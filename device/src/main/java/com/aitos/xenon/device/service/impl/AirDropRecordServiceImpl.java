package com.aitos.xenon.device.service.impl;

import com.aitos.common.crypto.ecdsa.Ecdsa;
import com.aitos.common.crypto.ecdsa.Hash;
import com.aitos.xenon.account.api.RemoteAccountService;
import com.aitos.xenon.account.api.RemoteKMSService;
import com.aitos.xenon.account.api.RemoteTokenService;
import com.aitos.xenon.account.api.RemoteTransactionService;
import com.aitos.xenon.account.api.domain.dto.AccountRegisterDto;
import com.aitos.xenon.account.api.domain.dto.RemoteKMSSignDto;
import com.aitos.xenon.account.api.domain.dto.TransactionDto;
import com.aitos.xenon.account.api.domain.vo.AccountVo;
import com.aitos.xenon.account.api.domain.vo.RemoteKMSSignVo;
import com.aitos.xenon.block.api.RemoteBlockService;
import com.aitos.xenon.block.api.RemoteSystemConfigService;
import com.aitos.xenon.block.api.domain.vo.SystemConfigVo;
import com.aitos.xenon.core.constant.ApiStatus;
import com.aitos.xenon.core.constant.BusinessConstants;
import com.aitos.xenon.core.constant.RandomLocation;
import com.aitos.xenon.core.constant.RandomLocationUtils;
import com.aitos.xenon.core.exceptions.ServiceException;
import com.aitos.xenon.core.exceptions.account.OwnerAccountNotExistException;
import com.aitos.xenon.core.exceptions.device.MinerApplyException;
import com.aitos.xenon.core.exceptions.device.MinerClaimVerifyException;
import com.aitos.xenon.core.exceptions.device.RecoverPublicKeyException;
import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.core.utils.BeanConvertor;
import com.aitos.xenon.core.utils.MetaMaskUtils;
import com.aitos.xenon.core.utils.SignatureProcessor;
import com.aitos.xenon.device.api.RemoteDeviceService;
import com.aitos.xenon.device.api.RemoteGameMinerService;
import com.aitos.xenon.device.api.domain.dto.*;
import com.aitos.xenon.device.api.domain.vo.DeviceVo;
import com.aitos.xenon.device.api.domain.vo.GameMiner;
import com.aitos.xenon.device.common.exception.MinerSingleNumberException;
import com.aitos.xenon.device.common.exception.MinerTotalNumberException;
import com.aitos.xenon.device.domain.AirDropRecord;
import com.aitos.xenon.device.domain.Device;
import com.aitos.xenon.device.mapper.AirDropRecordMapper;
import com.aitos.xenon.device.mapper.DeviceMapper;
import com.aitos.xenon.device.service.AirDropRecordService;
import com.aitos.xenon.fundation.api.RemoteFundationService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;

@Service
@Slf4j
public class AirDropRecordServiceImpl implements AirDropRecordService {

    @Autowired
    private AirDropRecordMapper airDropRecordMapper;
    @Autowired
    private RemoteBlockService blockService;
    @Autowired
    private RemoteTransactionService remoteTransactionService;
    @Autowired
    private RemoteBlockService remoteBlockService;
    @Autowired
    private RemoteAccountService remoteAccountService;
    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private RemoteDeviceService remoteDeviceService;
    @Autowired
    private RemoteGameMinerService remoteGameMinerService;
    @Autowired
    private RemoteSystemConfigService remoteSystemConfigService;

    @Autowired
    private RemoteKMSService remoteKMSService;

    @Value("${xenon.web.claim}")
    private String webClaimUrl;
    @Value("${xenon.airdrop.apply.personalSign}")
    private Boolean applyPersonalSign;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(AirDropDto airDropDto) {
        AirDropRecord airDropRecord = BeanConvertor.toBean(airDropDto, AirDropRecord.class);
        airDropRecord.setCreateTime(LocalDateTime.now());

        DeviceLocationDto locationDto = airDropDto.getLocation();
        airDropRecord.setLocationType(locationDto.getLocationType());
        airDropRecord.setLongitude(Double.valueOf(locationDto.getLongitude()));
        airDropRecord.setLatitude(Double.valueOf(locationDto.getLatitude()));
        airDropRecord.setH3index(locationDto.getH3index());

        DeviceInfoDto minerInfo = airDropDto.getMinerInfo();
        airDropRecord.setPower(minerInfo.getPower());
        airDropRecord.setCapabilities(minerInfo.getCapabilities());
        airDropRecord.setEnergy(minerInfo.getEnergy());
        airDropRecord.setDeviceModel(minerInfo.getDeviceModel());
        airDropRecord.setDeviceSerialNum(minerInfo.getDeviceSerialNum());
        airDropRecord.setStatus(BusinessConstants.DeviceAirdropStatus.NOT_CLAIM);

        Result<Long> blockHeightResult = blockService.getBlockHeight();
        if (blockHeightResult.getCode() != ApiStatus.SUCCESS.getCode()) {
            throw new ServiceException(blockHeightResult.getMsg());
        }
        //3天
        airDropRecord.setExpiration(blockHeightResult.getData() + 60 * 24 * 3);
        airDropRecordMapper.save(airDropRecord);


        Result<Long> blockVoResult = remoteBlockService.getBlockHeight();
        if (blockVoResult.getCode() != ApiStatus.SUCCESS.getCode()) {
            throw new ServiceException(blockVoResult.getMsg());
        }
        String txData = JSON.toJSONString(airDropDto);
        String txHash = DigestUtils.sha256Hex(txData);
        //记录交易信息
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setFromAddress(airDropDto.getMinerAddress());
        transactionDto.setHeight(blockVoResult.getData());
        transactionDto.setData(txData);

        transactionDto.setHash(txHash);
        transactionDto.setTxType(BusinessConstants.TXType.TX_AIRDROP_MINER);
        Result<String> transaction = remoteTransactionService.transaction(transactionDto);
        if (transaction.getCode() != ApiStatus.SUCCESS.getCode()) {
            throw new ServiceException(transaction.getMsg());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void claim(ClaimDto claimDto) {
        airDropRecordMapper.claim(claimDto);

        Result<AccountVo> ownerAccountVoResult = remoteAccountService.findByAddress(claimDto.getOwnerAddress());
        log.info("bind.ownerAccountVoResult=", JSON.toJSONString(ownerAccountVoResult));
        if (ownerAccountVoResult.getData() == null) {
            AccountRegisterDto accountRegisterDto = new AccountRegisterDto();
            accountRegisterDto.setAddress(claimDto.getOwnerAddress());
            accountRegisterDto.setAccountType(BusinessConstants.AccountType.WALLET);
            Result<Long> registerResult = remoteAccountService.register(accountRegisterDto);
            log.info("bind.registerResult=", JSON.toJSONString(registerResult));
            if (registerResult.getCode() != ApiStatus.SUCCESS.getCode()) {
                throw new OwnerAccountNotExistException("owner账户创建失败");
            }
        }

        AirDropRecord airDropRecord = findByMinerAddress(claimDto.getMinerAddress());

        Device device = new Device();
        device.setAddress(airDropRecord.getMinerAddress());
        device.setOwnerAddress(airDropRecord.getOwnerAddress());

        device.setLocationType(airDropRecord.getLocationType());
        device.setLatitude(airDropRecord.getLatitude());
        device.setLongitude(airDropRecord.getLongitude());
        device.setH3index(airDropRecord.getH3index());

        device.setEnergy(airDropRecord.getEnergy());
        device.setCapabilities(airDropRecord.getCapabilities());
        device.setPower(airDropRecord.getPower());
        device.setDeviceModel(airDropRecord.getDeviceModel());
        device.setDeviceSerialNum(airDropRecord.getDeviceSerialNum());
        device.setUpdateTime(LocalDateTime.now());
        device.setStatus(BusinessConstants.DeviceStatus.BOUND);
        deviceMapper.bind(device);

        Result<Long> blockVoResult = remoteBlockService.getBlockHeight();
        if (blockVoResult.getCode() != ApiStatus.SUCCESS.getCode()) {
            throw new ServiceException(blockVoResult.getMsg());
        }

        HashMap<String, Object> signatureData = new HashMap<>();
        signatureData.put("ownerAddress", claimDto.getOwnerAddress());
        signatureData.put("minerAddress", claimDto.getMinerAddress());
        String dataJson = JSON.toJSONString(signatureData);
        byte[] hash = Hash.sha3(dataJson.getBytes());
        RemoteKMSSignDto remoteKMSSignDto = new RemoteKMSSignDto();
        remoteKMSSignDto.setKeyId(BusinessConstants.TokenKeyId.PRIVILEGED);
        remoteKMSSignDto.setHash(Hex.toHexString(hash));
        remoteKMSSignDto.setRawData(dataJson);
        Result<RemoteKMSSignVo> signResult = remoteKMSService.sign(remoteKMSSignDto);
        log.info("claim.signResult:{}", JSON.toJSONString(signResult));
        if (signResult.getCode() != ApiStatus.SUCCESS.getCode()) {
            throw new ServiceException("miner apply failed");
        }
        RemoteKMSSignVo remoteKMSSignVo = signResult.getData();
        String sign = SignatureProcessor.signBuild(new SignatureProcessor.Signature(remoteKMSSignVo.getR(), remoteKMSSignVo.getS(), remoteKMSSignVo.getRecid()));
        //记录交易信息
        signatureData.put("signature", sign);
        String txData = JSON.toJSONString(signatureData);
        String txHash = DigestUtils.sha256Hex(txData);

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setFromAddress(device.getAddress());
        transactionDto.setHeight(blockVoResult.getData());
        transactionDto.setData(txData);

        transactionDto.setHash(txHash);
        transactionDto.setTxType(BusinessConstants.TXType.TX_CLAIM_MINER);
        Result<String> transaction = remoteTransactionService.transaction(transactionDto);
        if (transaction.getCode() != ApiStatus.SUCCESS.getCode()) {
            throw new ServiceException(transaction.getMsg());
        }


    }

    @Override
    public AirDropRecord findNotClaimedByMinerAddress(String minerAddress) {
        return airDropRecordMapper.findNotClaimedByMinerAddress(minerAddress);
    }

    @Override
    public AirDropRecord findByMinerAddress(String minerAddress) {
        return airDropRecordMapper.findByMinerAddress(minerAddress);
    }

    /**
     * 解析公钥并生成需要的其他数据
     *
     * @param str
     * @return
     */
    @Override
    public synchronized String applyGameMiner(String str) {
        ApplyGameMiner applyGameMiner = JSON.parseObject(str, ApplyGameMiner.class);
        // 当需要签名校验时进行验证
        if (applyPersonalSign) {
            //恢复owner公钥
            JSONObject obj = JSON.parseObject(str, Feature.OrderedField);
            obj.remove("personalSign");
            //
            byte[] message = MetaMaskUtils.getMessage(obj.toJSONString());
            byte[] srcPublicKey = Ecdsa.getPublicKey(message, applyGameMiner.getPersonalSign());
            String ownerAddress = Ecdsa.getAddress(srcPublicKey);
            log.info("applyGameMiner Recover ownerAddress:{}", ownerAddress);
            // 判断恢复的owner地址与报文内的是否一致
            if (!ownerAddress.equalsIgnoreCase(applyGameMiner.getOwner())) {
                log.info("remoteGameMinerService.register error: recover owner address not equals owner address");
                throw new MinerApplyException("miner apply failed");
            }
        }
        // 检查gaming miner数量限制
        Result<SystemConfigVo> configResult = remoteSystemConfigService.findConfig();
        if (configResult.getCode() != ApiStatus.SUCCESS.getCode()) {
            throw new ServiceException("system config exception");
        }
        SystemConfigVo systemConfigVo = configResult.getData();
        int gameMinerCount = deviceMapper.countByMinerType(BusinessConstants.DeviceMinerType.GAME_MINER);
        if (gameMinerCount >= systemConfigVo.getGameMinerTotalNumber()) {
            throw new MinerTotalNumberException("miner 总量超出 限制");
        }

        int gameMinerSingleCount = deviceMapper.countByAddressAndMinerType(applyGameMiner.getOwner(), BusinessConstants.DeviceMinerType.GAME_MINER);
        if (gameMinerSingleCount >= systemConfigVo.getGameMinerSingleNumber()) {
            throw new MinerSingleNumberException("单个用户[地址]可申请miner量超出限制");
        }
        int emailSingleCount = airDropRecordMapper.countByEmail(applyGameMiner.getEmail());
        if (emailSingleCount >= systemConfigVo.getGameMinerSingleNumber()) {
            throw new MinerSingleNumberException("单个用户[邮箱]可申请miner量超出限制");
        }
        // 调用game miner服务生成miner地址进行预注册
        Result<String> gameMinerResult = remoteGameMinerService.register();
        if (gameMinerResult == null || gameMinerResult.getCode() != ApiStatus.SUCCESS.getCode()) {
            log.info("remoteGameMinerService.register error:{}", JSON.toJSONString(gameMinerResult));
            throw new MinerApplyException("miner apply failed");
        }
        log.info("remoteGameMinerService.register result:{}", JSON.toJSONString(gameMinerResult));
        // 发送miner信息到基金会签名
        String minerAddress = gameMinerResult.getData();
        DeviceRegisterDto deviceRegisterDto = new DeviceRegisterDto();
        deviceRegisterDto.setAddress(minerAddress);
        deviceRegisterDto.setMaker(BusinessConstants.MakerInfo.GAME_MINER);
        deviceRegisterDto.setVersion(1);
        deviceRegisterDto.setMinerType(BusinessConstants.DeviceMinerType.GAME_MINER);
        deviceRegisterDto.setTxData(applyGameMiner.getPersonalSign());
        //TODO 适配device register 只签名一个字段
        //Result<String> fundationRegister = remoteFundationService.register(deviceRegisterDto.getAddress());
        byte[] hash = Hash.sha3(deviceRegisterDto.getAddress().getBytes());
        RemoteKMSSignDto remoteKMSSignDto = new RemoteKMSSignDto();
        remoteKMSSignDto.setKeyId(BusinessConstants.TokenKeyId.PRIVILEGED);
        remoteKMSSignDto.setHash(Hex.toHexString(hash));
        remoteKMSSignDto.setRawData(deviceRegisterDto.getAddress());
        Result<RemoteKMSSignVo> signResult = remoteKMSService.sign(remoteKMSSignDto);
        log.info("remoteFundationService.register result:{}", JSON.toJSONString(signResult));
        if (signResult.getCode() != ApiStatus.SUCCESS.getCode()) {
            log.info("remoteFundationService.register error:{}", JSON.toJSONString(signResult));
            throw new MinerApplyException("miner apply failed");
        }
        RemoteKMSSignVo remoteKMSSignVo = signResult.getData();
        String sign = SignatureProcessor.signBuild(new SignatureProcessor.Signature(remoteKMSSignVo.getR(), remoteKMSSignVo.getS(), remoteKMSSignVo.getRecid()));
        deviceRegisterDto.setFoundationSignature(sign);
        deviceRegisterDto.setEmail(applyGameMiner.getEmail());
        //调用 miner register
        Result deviceRegister = remoteDeviceService.register(deviceRegisterDto);
        if (deviceRegister.getCode() != ApiStatus.SUCCESS.getCode()) {
            log.info("remoteDeviceService.register error:{}", JSON.toJSONString(deviceRegister));
            throw new MinerApplyException("miner apply failed");
        }
        //TODO 调用Miner AirDrop，部分数值暂时写死
        AirDropDto airDropDto = new AirDropDto();
        airDropDto.setMinerAddress(minerAddress);
        airDropDto.setOwnerAddress(applyGameMiner.getOwner());
        airDropDto.setEmail(applyGameMiner.getEmail());
        airDropDto.setVersion(1);
        // 30天
        Result<Long> blockHeight = remoteBlockService.getBlockHeight();
        airDropDto.setExpiration(blockHeight.getData() + (systemConfigVo.getGameMinerApplyValidityPeriod() * 24 * 60));
        // 随机地理位置
        DeviceLocationDto deviceLocationDto = new DeviceLocationDto();
        RandomLocation randomLocation = RandomLocationUtils.getRandomLocation();
        deviceLocationDto.setLocationType(0);
        deviceLocationDto.setLatitude(randomLocation.getLatitude() + "");
        deviceLocationDto.setLongitude(randomLocation.getLongitude() + "");
        airDropDto.setLocation(deviceLocationDto);
        log.info("DeviceLocationDto:{}", JSON.toJSONString(deviceLocationDto));
        //TODO 这里填写啥？
        DeviceInfoDto deviceInfoDto = new DeviceInfoDto();
        deviceInfoDto.setAddress(minerAddress);
        deviceInfoDto.setCapabilities(0);
        deviceInfoDto.setPower(0L);
        deviceInfoDto.setVersion(1);
        deviceInfoDto.setEnergy(1);

        airDropDto.setMinerInfo(deviceInfoDto);
        String airJson = JSON.toJSONString(airDropDto);

        byte[] hash2 = Hash.sha3(airJson.getBytes());
        RemoteKMSSignDto remoteKMSSignDto2 = new RemoteKMSSignDto();
        remoteKMSSignDto2.setKeyId(BusinessConstants.TokenKeyId.PRIVILEGED);
        remoteKMSSignDto2.setHash(Hex.toHexString(hash2));
        remoteKMSSignDto2.setRawData(deviceRegisterDto.getAddress());
        Result<RemoteKMSSignVo> signResult2 = remoteKMSService.sign(remoteKMSSignDto2);
        log.info("remoteFundationService.register signResult2:{}", JSON.toJSONString(signResult2));
        if (signResult2.getCode() != ApiStatus.SUCCESS.getCode()) {
            log.info("remoteFundationService.register error:{}", JSON.toJSONString(signResult2));
            throw new MinerApplyException("miner apply failed");
        }
        RemoteKMSSignVo remoteKMSSignVo2 = signResult2.getData();
        String sign2 = SignatureProcessor.signBuild(new SignatureProcessor.Signature(remoteKMSSignVo2.getR(), remoteKMSSignVo2.getS(), remoteKMSSignVo2.getRecid()));

        log.info("remoteFundationService.airdrop result:{}", signResult2.getData());
        JSONObject jsonObject = JSONObject.parseObject(airJson, Feature.OrderedField);
        //
        jsonObject.put("signature", sign2);
        String deviceAirdropStr = jsonObject.toJSONString();
        log.info("remoteFundationService.airdrop str:{}", airJson);
        log.info("remoteDeviceService.airdrop str:{}", deviceAirdropStr);
        Result airdrop = remoteDeviceService.airdrop(deviceAirdropStr);
        if (airdrop.getCode() != ApiStatus.SUCCESS.getCode()) {
            log.info("remoteDeviceService.airdrop error:{}", JSON.toJSONString(deviceRegister));
            throw new MinerApplyException("miner airdrop failed");
        }
        //TODO 发送邮件等通知owner
        HashMap<String, String> hashMap = new HashMap();
        hashMap.put("minerAddress", minerAddress);
        hashMap.put("ownerAddress", applyGameMiner.getOwner());
        //log.info("领取地址：http://localhost:8080/claim/"+ Base64Utils.encodeToString(JSON.toJSONString(hashMap).getBytes(StandardCharsets.UTF_8)));
        String claimGameMinerUrl = webClaimUrl + Base64Utils.encodeToString(JSON.toJSONString(hashMap).getBytes(StandardCharsets.UTF_8));
        log.info("领取地址：{}", claimGameMinerUrl);
        try {
            PushMessageDto pushMessageDto = new PushMessageDto();
            pushMessageDto.setTemplateId(1L);
            pushMessageDto.setTitile("You Game Miner Apply Result");
            pushMessageDto.setTo(applyGameMiner.getEmail());
            HashMap<String, Object> customMap = new HashMap<>();
            pushMessageDto.setCustomMap(customMap);
            customMap.put("url", claimGameMinerUrl);
            customMap.put("owner", applyGameMiner.getOwner());
            customMap.put("miner", minerAddress);
            Result result = remoteGameMinerService.pushMail(pushMessageDto);
            log.info("邮件发送结果:{}", JSON.toJSONString(result));
        } catch (Exception e) {
            log.error("邮件发送失败！");
        }
        //
        return ApiStatus.SUCCESS.getMsg();
    }

    /**
     * 申领gaming miner
     *
     * @param claimGameMiner
     * @return
     */
    @Override
    public String claimGameMiner(String claimGameMiner) {
        //
        Result claim = remoteDeviceService.claim(claimGameMiner);
        log.info("remoteDeviceService.claim:{}", JSON.toJSONString(claim));
        if (claim.getCode() != ApiStatus.SUCCESS.getCode()) {
            throw new MinerClaimVerifyException(claim.getMsg());
        }
        ClaimDto claimDto = JSON.parseObject(claimGameMiner, ClaimDto.class);
        GameMiner gameMiner = new GameMiner();
        gameMiner.setAddress(claimDto.getMinerAddress());
        //
        DeviceVo deviceVo = deviceMapper.queryByMiner(claimDto.getMinerAddress());
        gameMiner.setLatitude(deviceVo.getLatitude());
        gameMiner.setLongitude(deviceVo.getLongitude());
        //
        Result start = remoteGameMinerService.start(gameMiner);
        log.info("remoteGameMinerService.start:{}", JSON.toJSONString(start));
        if (start.getCode() != ApiStatus.SUCCESS.getCode()) {
            return "success";
        }
        return null;
    }
}
