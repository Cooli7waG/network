package com.aitos.xenon.device.service.impl;

import com.aitos.common.crypto.ecdsa.Ecdsa;
import com.aitos.xenon.account.api.RemoteAccountService;
import com.aitos.xenon.account.api.RemoteTransactionService;
import com.aitos.xenon.account.api.domain.dto.AccountRegisterDto;
import com.aitos.xenon.account.api.domain.dto.TransactionDto;
import com.aitos.xenon.account.api.domain.vo.AccountVo;
import com.aitos.xenon.block.api.RemoteBlockService;
import com.aitos.xenon.core.constant.ApiStatus;
import com.aitos.xenon.core.constant.BusinessConstants;
import com.aitos.xenon.core.exceptions.ServiceException;
import com.aitos.xenon.core.exceptions.account.OwnerAccountNotExistException;
import com.aitos.xenon.core.exceptions.device.DeviceExistedException;
import com.aitos.xenon.core.exceptions.device.MinerApplyException;
import com.aitos.xenon.core.exceptions.device.MinerClaimVerifyException;
import com.aitos.xenon.core.exceptions.device.RecoverPublicKeyException;
import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.core.utils.BeanConvertor;
import com.aitos.xenon.device.api.RemoteDeviceService;
import com.aitos.xenon.device.api.RemoteGameMinerService;
import com.aitos.xenon.device.api.domain.dto.*;
import com.aitos.xenon.device.api.domain.vo.DeviceVo;
import com.aitos.xenon.device.api.domain.vo.GameMiner;
import com.aitos.xenon.device.domain.Device;
import com.aitos.xenon.device.mapper.DeviceMapper;
import com.aitos.xenon.device.service.DeviceService;
import com.aitos.xenon.fundation.api.RemoteFundationService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private RemoteAccountService remoteAccountService;

    @Autowired
    private RemoteTransactionService remoteTransactionService;

    @Autowired
    private RemoteBlockService remoteBlockService;
    @Autowired
    private RemoteFundationService remoteFundationService;
    @Autowired
    private RemoteDeviceService remoteDeviceService;
    @Autowired
    private RemoteGameMinerService remoteGameMinerService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(DeviceRegisterDto deviceRegister) {
        Device device= BeanConvertor.toBean(deviceRegister,Device.class);

        device.setEarningMint(BigDecimal.valueOf(0));
        device.setEarningService(BigDecimal.valueOf(0));
        deviceMapper.save(device);

        AccountRegisterDto accountRegisterDto =new AccountRegisterDto();
        accountRegisterDto.setAddress(device.getAddress());
        accountRegisterDto.setAccountType(BusinessConstants.AccountType.MINER);
        accountRegisterDto.setBalance(new BigDecimal("0"));
        accountRegisterDto.setNonce(0L);
        Result result=remoteAccountService.register(accountRegisterDto);
        if(result.getCode()!= ApiStatus.SUCCESS.getCode()){

           throw new DeviceExistedException("设备账户已经存在");
        }

        Result<Long> blockVoResult= remoteBlockService.getBlockHeight();
        //记录交易信息
        TransactionDto transactionDto=new TransactionDto();
        transactionDto.setFromAddress(device.getAddress());
        transactionDto.setHeight(blockVoResult.getData());
        transactionDto.setData(deviceRegister.getTxData());
        String txHash=DigestUtils.sha256Hex(deviceRegister.getTxData());
        transactionDto.setHash(txHash);
        transactionDto.setTxType(BusinessConstants.TXType.TX_REGISTER_MINER);
        remoteTransactionService.transaction(transactionDto);
    }

    @Override
    public Device findByAddress(String address) {
        Device  device= deviceMapper.findByAddress(address);
        return device;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bind(DeviceBindDto deviceBindDto) {
        //判断payer的金额是否满足
        /*Result<AccountVo>  payerAccountVoResult= remoteAccountService.findByAddress(deviceBindDto.getPayerAddress());
        AccountVo payerAccountVo=payerAccountVoResult.getData();
        if(payerAccountVo==null){
            throw new PayerAccountNotExistException("付款方账户不存在");
        }*/
        /*if(payerAccountVo.getBalance().subtract(deviceBindDto.getStakingFee()).compareTo(new BigInteger("0"))>0){
            throw new PayerAccountNotEnoughException("账户额度不够");
        }*/

        Result<AccountVo>  ownerAccountVoResult= remoteAccountService.findByAddress(deviceBindDto.getOwnerAddress());
        log.info("bind.ownerAccountVoResult=", JSON.toJSONString(ownerAccountVoResult));
        if(ownerAccountVoResult.getData()==null){
            AccountRegisterDto accountRegisterDto=new AccountRegisterDto();
            accountRegisterDto.setAddress(deviceBindDto.getOwnerAddress());
            accountRegisterDto.setAccountType(BusinessConstants.AccountType.WALLET);
            Result<Long> registerResult=remoteAccountService.register(accountRegisterDto);
            log.info("bind.registerResult=", JSON.toJSONString(registerResult));
            if(registerResult.getCode()!=ApiStatus.SUCCESS.getCode()){
                throw new OwnerAccountNotExistException("owner账户创建失败");
            }
        }

        Device device=new Device();
        device.setId(deviceBindDto.getDeviceId());
        device.setAddress(deviceBindDto.getMinerAddress());
        device.setOwnerAddress(deviceBindDto.getOwnerAddress());

        DeviceLocationDto deviceLocationDto=deviceBindDto.getLocation();
        device.setLocationType(deviceLocationDto.getLocationType());
        device.setLatitude(deviceLocationDto.getLatitude());
        device.setLongitude(deviceLocationDto.getLongitude());
        device.setH3index(deviceLocationDto.getH3index());

        DeviceInfoDto minerInfo = deviceBindDto.getMinerInfo();
        device.setEnergy(minerInfo.getEnergy());
        device.setCapabilities(minerInfo.getCapabilities());
        device.setPower(minerInfo.getPower());
        device.setDeviceModel(minerInfo.getDeviceModel());
        device.setDeviceSerialNum(minerInfo.getDeviceSerialNum());
        device.setUpdateTime(LocalDateTime.now());
        deviceMapper.bind(device);

        Result<Long> blockVoResult= remoteBlockService.getBlockHeight();
        if(blockVoResult.getCode()!= ApiStatus.SUCCESS.getCode()){
            throw new ServiceException(blockVoResult.getMsg());
        }
        //记录交易信息
        TransactionDto transactionDto=new TransactionDto();
        transactionDto.setFromAddress(device.getAddress());
        transactionDto.setHeight(blockVoResult.getData());
        transactionDto.setData(deviceBindDto.getTxData());

        transactionDto.setHash(deviceBindDto.getTxHash());
        transactionDto.setTxType(BusinessConstants.TXType.TX_ONBOARD_MINER);
        Result<String> transaction = remoteTransactionService.transaction(transactionDto);
        if(transaction.getCode()!= ApiStatus.SUCCESS.getCode()){
            throw new ServiceException(transaction.getMsg());
        }
    }

    @Override
    public void update(Device device) {
        device.setUpdateTime(LocalDateTime.now());
        deviceMapper.update(device);
    }

    @Override
    public IPage<DeviceVo> list(DeviceSearchDto queryParams) {
        Page<DeviceVo> page=new Page<DeviceVo>(queryParams.getOffset(),queryParams.getLimit());
        IPage<DeviceVo> pageResult=deviceMapper.list(page,queryParams);
        return pageResult;
    }

    @Override
    public List<Device> queryByOwner(String ownerAddress) {
        List<Device> list=deviceMapper.findByOwnerAddress(ownerAddress);
        return list;
    }

    @Override
    public DeviceVo queryByMiner(String minerAddress) {
        DeviceVo device=deviceMapper.queryByMiner(minerAddress);
        return device;
    }

    @Override
    public void terminate(DeviceTerminateMinerDto deviceTerminateMinerDto) {
        deviceMapper.terminate(deviceTerminateMinerDto);
    }

    /**
     * 解析公钥并生成需要的其他数据
     * @param applyGameMiner
     * @return
     */
    @Override
    public String applyGameMiner(ApplyGameMiner applyGameMiner) {
        String str =applyGameMiner.getName()+"("+applyGameMiner.getEmail()+") Request Game Miner";
        byte[] srcPublicKey =null;
        try{
            //恢复owner公钥
            srcPublicKey = Ecdsa.getPublicKey( str.getBytes(),applyGameMiner.getPersonalSign());
            log.info("RecoverPublicKeyUtils.recoverPublicKeyHexString:{}",srcPublicKey);
            /*byte[] bytes = srcPublicKey.getBytes(StandardCharsets.UTF_8);
            byte[] xenonBytes = new byte[bytes.length+2];
            System.arraycopy(bytes,0,xenonBytes,2,bytes.length);
            xenonBytes[0] = 0x00;
            xenonBytes[1] = 0x01;*/
            String ownerAddress = Ecdsa.getAddress(srcPublicKey);
            log.info("applyGameMiner Recover ownerAddress:{}",ownerAddress);
            // 调用game miner服务生成miner地址进行预注册
            Result<String> gameMinerResult = remoteGameMinerService.register();
            if(gameMinerResult==null || gameMinerResult.getCode() != ApiStatus.SUCCESS.getCode()){
                log.info("remoteGameMinerService.register error:{}",JSON.toJSONString(gameMinerResult));
                throw new MinerApplyException("miner apply failed");
            }
            log.info("remoteGameMinerService.register result:{}",JSON.toJSONString(gameMinerResult));
            // 发送miner信息到基金会签名
            String minerAddress = gameMinerResult.getData();
            DeviceRegisterDto deviceRegisterDto = new DeviceRegisterDto();
            deviceRegisterDto.setAddress(minerAddress);
            deviceRegisterDto.setMaker(BusinessConstants.MakerInfo.GAME_MINER);
            deviceRegisterDto.setVersion(1);
            deviceRegisterDto.setMinerType(BusinessConstants.DeviceMinerType.GAME_MINER);
            deviceRegisterDto.setTxData(applyGameMiner.getPersonalSign());
            //TODO 适配device register 只签名一个字段
            Result<String> register = remoteFundationService.register(deviceRegisterDto.getAddress());
            log.info("remoteFundationService.register result:{}",register.getData());
            deviceRegisterDto.setFoundationSignature(register.getData());
            //调用 miner register
            Result deviceRegister = remoteDeviceService.register(deviceRegisterDto);
            if(deviceRegister.getCode() != ApiStatus.SUCCESS.getCode()){
                log.info("remoteDeviceService.register error:{}",JSON.toJSONString(deviceRegister));
                throw new MinerApplyException("miner apply failed");
            }
            //TODO 调用Miner AirDrop，部分数值暂时写死
            AirDropDto airDropDto = new AirDropDto();
            airDropDto.setMinerAddress(minerAddress);
            airDropDto.setOwnerAddress(ownerAddress);
            airDropDto.setVersion(1);
            // 30天
            Result<Long> blockHeight = remoteBlockService.getBlockHeight();
            airDropDto.setExpiration(blockHeight.getData()+(30*24*60));
            //TODO 来源？
            DeviceLocationDto deviceLocationDto = new DeviceLocationDto();
            airDropDto.setLocation(deviceLocationDto);
            //TODO 这里填写啥？
            DeviceInfoDto deviceInfoDto = new DeviceInfoDto();
            deviceInfoDto.setAddress(minerAddress);
            deviceInfoDto.setCapabilities("0");
            deviceInfoDto.setPower(0L);
            deviceInfoDto.setVersion(1);
            deviceInfoDto.setEnergy(1);

            airDropDto.setMinerInfo(deviceInfoDto);
            String airJson = JSON.toJSONString(airDropDto);
            Result<String> airdropResult = remoteFundationService.airdrop(airJson);
            log.info("remoteFundationService.airdrop result:{}",airdropResult.getData());
            String airdropSign = airdropResult.getData();
            JSONObject jsonObject=JSONObject.parseObject(airJson, Feature.OrderedField);
            //
            jsonObject.put("signature",airdropSign);
            String deviceAirdropStr = jsonObject.toJSONString();
            log.info("remoteFundationService.airdrop str:{}",airJson);
            log.info("remoteDeviceService.airdrop str:{}",deviceAirdropStr);
            Result airdrop = remoteDeviceService.airdrop(deviceAirdropStr);
            if(airdrop.getCode() != ApiStatus.SUCCESS.getCode()){
                log.info("remoteDeviceService.airdrop error:{}",JSON.toJSONString(deviceRegister));
                throw new MinerApplyException("miner airdrop failed");
            }
            //TODO 发送邮件等通知owner
            HashMap<String,String> hashMap = new HashMap();
            hashMap.put("minerAddress",minerAddress);
            hashMap.put("ownerAddress",ownerAddress);
            log.info("领取地址：http://localhost:8080/claim/"+ Base64Utils.encodeToString(JSON.toJSONString(hashMap).getBytes(StandardCharsets.UTF_8)));
            //
            return ApiStatus.SUCCESS.getMsg();
        }catch (Exception e){
            log.error("Apply Game Miner Error:{}",e.getMessage());
            throw new RecoverPublicKeyException("Failed to get user address");
        }
    }

    /**
     * 申领gaming miner
     * @param claimGameMiner
     * @return
     */
    @Override
    public String claimGameMiner(String claimGameMiner) {
        //
        Result claim = remoteDeviceService.claim(claimGameMiner);
        log.info("remoteDeviceService.claim:{}",JSON.toJSONString(claim));
        if(claim.getCode() != ApiStatus.SUCCESS.getCode()){
            throw new MinerClaimVerifyException("claim miner airdrop verify failed");
        }
        ClaimDto claimDto=JSON.parseObject(claimGameMiner,ClaimDto.class);
        GameMiner gameMiner = new GameMiner();
        gameMiner.setAddress(claimDto.getMinerAddress());
        Result start = remoteGameMinerService.start(gameMiner);
        log.info("remoteGameMinerService.start:{}",JSON.toJSONString(start));
        if(start.getCode() != ApiStatus.SUCCESS.getCode()){
            return "success";
        }
        return null;
    }
}
