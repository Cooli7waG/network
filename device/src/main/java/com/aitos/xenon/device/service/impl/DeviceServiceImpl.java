package com.aitos.xenon.device.service.impl;

import com.aitos.common.crypto.ecdsa.Ecdsa;
import com.aitos.message.push.api.RemotePushService;
import com.aitos.message.push.api.domain.dto.PushMessageDto;
import com.aitos.xenon.account.api.RemoteAccountRewardService;
import com.aitos.xenon.account.api.RemoteAccountService;
import com.aitos.xenon.account.api.RemoteTransactionService;
import com.aitos.xenon.account.api.domain.dto.AccountRegisterDto;
import com.aitos.xenon.account.api.domain.dto.TransactionDto;
import com.aitos.xenon.account.api.domain.vo.AccountRewardStatisticsVo;
import com.aitos.xenon.account.api.domain.vo.AccountVo;
import com.aitos.xenon.block.api.RemoteBlockService;
import com.aitos.xenon.block.api.RemotePoggService;
import com.aitos.xenon.core.constant.ApiStatus;
import com.aitos.xenon.core.constant.BusinessConstants;
import com.aitos.xenon.core.constant.RandomLocation;
import com.aitos.xenon.core.constant.RandomLocationUtils;
import com.aitos.xenon.core.exceptions.ServiceException;
import com.aitos.xenon.core.exceptions.account.OwnerAccountNotExistException;
import com.aitos.xenon.core.exceptions.device.DeviceExistedException;
import com.aitos.xenon.core.exceptions.device.MinerApplyException;
import com.aitos.xenon.core.exceptions.device.MinerClaimVerifyException;
import com.aitos.xenon.core.exceptions.device.RecoverPublicKeyException;
import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.core.utils.BeanConvertor;
import com.aitos.xenon.core.utils.Location;
import com.aitos.xenon.core.utils.LocationTransformUtils;
import com.aitos.xenon.core.utils.MetaMaskUtils;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private RemotePoggService remotePoggService;

    @Autowired
    private RemoteAccountRewardService remoteAccountRewardService;

    private static HashMap MINER_LOCATION_CACHE = new HashMap();

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(DeviceRegisterDto deviceRegister) {
        Device device= BeanConvertor.toBean(deviceRegister,Device.class);

        device.setEarningMint(BigDecimal.valueOf(0));
        device.setEarningService(BigDecimal.valueOf(0));
        device.setCreateTime(LocalDateTime.now());
        deviceMapper.save(device);
        //
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
        if (device != null) {
            Result<Double> avgPowerResult = remotePoggService.avgPower(minerAddress);
            log.info("queryByMiner.avgPowerResult={}",JSON.toJSONString(avgPowerResult));
            if(avgPowerResult.getCode()==ApiStatus.SUCCESS.getCode()){
                device.setAvgPower(avgPowerResult.getData());
            }else{
                device.setAvgPower(0D);
            }
            Result<AccountRewardStatisticsVo> accountStatistics = remoteAccountRewardService.statistics(device.getAddress());
            log.info("queryByMiner.result={}",JSON.toJSONString(accountStatistics));
            if(accountStatistics.getCode()==ApiStatus.SUCCESS.getCode()){
                AccountRewardStatisticsVo accountRewardStatisticsVo = accountStatistics.getData();
                device.setTotalReward(accountRewardStatisticsVo.getTotalReward());
                device.setAvgReward(accountRewardStatisticsVo.getAvgReward());
                device.setTodayReward(accountRewardStatisticsVo.getTodayReward());
                device.setYesterdayReward(accountRewardStatisticsVo.getYesterdayReward());
            }else{
                device.setTotalReward(BigDecimal.valueOf(0));
                device.setAvgReward(BigDecimal.valueOf(0));
                device.setTodayReward(BigDecimal.valueOf(0));
                device.setYesterdayReward(BigDecimal.valueOf(0));
            }
        }
        return device;
    }

    @Override
    public void terminate(DeviceTerminateMinerDto deviceTerminateMinerDto) {
        deviceMapper.terminate(deviceTerminateMinerDto);
    }

    /**
     * 根据owner地址查询miner列表
     * @param queryParams
     * @return
     */
    @Override
    public IPage<DeviceVo> getMinersByOwnerAddress(DeviceSearchDto queryParams) {
        Page<DeviceVo> page=new Page<DeviceVo>(queryParams.getOffset(),queryParams.getLimit());
        IPage<DeviceVo> pageResult=deviceMapper.getMinersByOwnerAddress(page,queryParams);
        return pageResult;
    }

    /**
     * 获取所有miner位置信息
     * @return
     */
    @Override
    public HashMap getMinerLocation() {
        if(MINER_LOCATION_CACHE.size()==0){
            List<DeviceVo> deviceList = deviceMapper.getAllMinerLocation();
            for (DeviceVo deviceVo : deviceList) {
                Location location = new Location();
                location.setLatitude(deviceVo.getLatitude());
                location.setLongitude(deviceVo.getLongitude());
                if(deviceVo.getLatitude()!=null && deviceVo.getLongitude()!=null){
                    MINER_LOCATION_CACHE.put(deviceVo.getAddress(),location);
                }
            }
        }
        return MINER_LOCATION_CACHE;
    }

    @Override
    public List<DeviceVo> loadMinersInfo(ArrayList<String> addressList) {
        List<DeviceVo> deviceList = new ArrayList<>();
        for (String address : addressList) {
            DeviceVo byAddress = queryByMiner(address);
            deviceList.add(byAddress);
        }
        return deviceList;
    }

    /**
     * 根据owner查询miner列表
     * @param address
     * @return
     */
    @Override
    public List<DeviceVo> getMinerListByOwner(String address) {
        return deviceMapper.getMinerListByOwner(address);
    }
}
