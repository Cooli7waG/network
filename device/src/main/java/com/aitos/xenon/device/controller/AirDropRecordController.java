package com.aitos.xenon.device.controller;

import com.aitos.common.crypto.coder.DataCoder;
import com.aitos.common.crypto.ecdsa.Ecdsa;
import com.aitos.xenon.account.api.RemoteTokenService;
import com.aitos.xenon.account.api.domain.dto.TokenServiceNftSignDto;
import com.aitos.xenon.block.api.RemoteBlockService;
import com.aitos.xenon.core.constant.ApiStatus;
import com.aitos.xenon.core.constant.BusinessConstants;
import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.core.utils.BeanConvertor;
import com.aitos.xenon.core.utils.Location;
import com.aitos.xenon.core.utils.MetaMaskUtils;
import com.aitos.xenon.device.api.domain.dto.AirDropDto;
import com.aitos.xenon.device.api.domain.dto.ClaimDto;
import com.aitos.xenon.device.api.domain.dto.NftSignDto;
import com.aitos.xenon.device.api.domain.vo.DeviceVo;
import com.aitos.xenon.device.domain.AirDropRecord;
import com.aitos.xenon.device.domain.Device;
import com.aitos.xenon.device.service.AirDropRecordService;
import com.aitos.xenon.device.service.DeviceService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/airdroprecord")
@Slf4j
@RefreshScope
public class AirDropRecordController {

    @Autowired
    private AirDropRecordService airDropRecordService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private RemoteBlockService blockService;

    @Autowired
    private RemoteTokenService remoteTokenService;

    @Value("${foundation.publicKey}")
    private String foundationPublicKey;
    @Value("${xenon.airdrop.apply.active}")
    private Boolean applyActive;
    @Value("${xenon.airdrop.apply.personalSign}")
    private Boolean applyPersonalSign;

    @PostMapping("/airdrop")
    public Result airDrop(@RequestBody String body){
        log.info("airdrop.body:{}",body);
        JSONObject jsonObject=JSONObject.parseObject(body, Feature.OrderedField);
        String signature=jsonObject.getString("signature");
        jsonObject.remove("signature");
        String jsonData=jsonObject.toJSONString();
        log.info("AirDropRecordController.airDrop jsonData:{}",jsonData);
        log.info("AirDropRecordController.airDrop signature:{}",signature);
        AirDropDto airDropDto=JSON.parseObject(body,AirDropDto.class);
        Boolean verify= Ecdsa.verifyByAddress(foundationPublicKey,jsonData,signature,DataCoder.BASE58);
        if(!verify){
            return Result.failed(ApiStatus.VALIDATE_SIGN_FAILED);
        }
        //检查设备状态
        Device device = deviceService.findByAddress(airDropDto.getMinerAddress());
        if(device==null){
            return Result.failed(ApiStatus.BUSINESS_DEVICE_NOT_EXISTED);
        }else if(device.getStatus()== BusinessConstants.DeviceStatus.BOUND){
            return Result.failed(ApiStatus.BUSINESS_DEVICE_BOUND);
        }
        //检查空投状态
        AirDropRecord airDropRecordTemp=airDropRecordService.findNotClaimedByMinerAddress(airDropDto.getMinerAddress());
        if(airDropRecordTemp!=null && airDropRecordTemp.getStatus()== BusinessConstants.DeviceAirdropStatus.NOT_CLAIM){
            return Result.failed(ApiStatus.BUSINESS_AIRDROPDEVICE_EXISTED);
        }
        /*Result<Long> blockHeightResult = blockService.getBlockHeight();
        if(blockHeightResult.getCode()== ApiStatus.SUCCESS.getCode()&&
                airDropRecordTemp.getExpiration()<=blockHeightResult.getData()
        ){
            return Result.failed(ApiStatus.BUSINESS_AIRDROPDEVICE_CLAIM_EXPIRED);
        }*/
        airDropRecordService.save(airDropDto);
        //更新缓存的miner地理位置
        try{
            deviceService.getMinerLocation();
        }catch (Exception e){
            log.error("更新地理位置失败：{}",e.getMessage());
        }
        return Result.ok();
    }

    @PostMapping("/claim")
    public Result claim(@RequestBody String body) throws Exception {
        log.info("claim.body:{}",body);
        JSONObject jsonObject=JSONObject.parseObject(body, Feature.OrderedField);
        String signature = jsonObject.getString("signature");
        jsonObject.remove("signature");
        String jsonData=jsonObject.toJSONString();
        //
        ClaimDto claimDto=JSON.parseObject(body,ClaimDto.class);
        log.info("ClaimDto owner address:{}",claimDto.getOwnerAddress());
        //
        byte[] message = MetaMaskUtils.getMessage(jsonData);
        byte[] srcPublicKey = Ecdsa.getPublicKey(message,signature);
        Boolean verify = Ecdsa.verifyByPublicKey(srcPublicKey,message,signature);
        //Boolean verify= Ecdsa.verifyByAddress(srcPublicKey,message,signature, null);
        if(!verify){
            return Result.failed(ApiStatus.VALIDATE_SIGN_FAILED);
        }
        //检查设备状态
        Device device = deviceService.findByAddress(claimDto.getMinerAddress());
        if(device==null){
            return Result.failed(ApiStatus.BUSINESS_DEVICE_NOT_EXISTED);
        }else if(device.getStatus()== BusinessConstants.DeviceStatus.BOUND){
            return Result.failed(ApiStatus.BUSINESS_DEVICE_BOUND);
        }
        //检查空投状态
        AirDropRecord airDropRecordTemp=airDropRecordService.findNotClaimedByMinerAddress(claimDto.getMinerAddress());
        if(airDropRecordTemp==null){
            return Result.failed(ApiStatus.BUSINESS_AIRDROPDEVICE_NOT_EXISTED);
        }
        Result<Long> blockHeightResult = blockService.getBlockHeight();
        if(airDropRecordTemp!=null&&airDropRecordTemp.getExpiration()<blockHeightResult.getData()){
            return Result.failed(ApiStatus.BUSINESS_AIRDROPDEVICE_CLAIM_EXPIRED);
        }
        airDropRecordService.claim(claimDto);
        return Result.ok();
    }

    @PostMapping("/gameminer/apply")
    public Result applyGameMiner(@RequestBody String applyGameMiner){
        if(!applyActive){
            return Result.failed(ApiStatus.BUSINESS_AIRDROP_NOT_ACTIVE.getMsg());
        }
        log.info("applyGameMiner:{}",applyGameMiner);
        String result = airDropRecordService.applyGameMiner(applyGameMiner);
        return Result.ok(result);
    }

    @PostMapping("/gameminer/claim")
    public Result claimGameMiner(@RequestBody String claimGameMiner){
        log.info("DeviceController.claimGameMiner:{}",claimGameMiner);
        String result = airDropRecordService.claimGameMiner(claimGameMiner);
        return Result.ok(result);
    }

    @PostMapping("/applyAirdrop")
    public Result getApplyActiveInfo(){
        HashMap<String,Boolean> map=new HashMap();
        map.put("applyActive",applyActive);
        map.put("applyPersonalSign",applyPersonalSign);
        return Result.ok(map);
    }

    @PostMapping("/nftsign")
    public Result nftsign(@RequestBody NftSignDto nftSignDto){
        TokenServiceNftSignDto tokenServiceNftSignDto= BeanConvertor.toBean(nftSignDto,TokenServiceNftSignDto.class);

        long timestamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()/1000+7*24*60*60;
        tokenServiceNftSignDto.setDeadline(timestamp);
        log.info("nftSignature.params={}",JSON.toJSONString(tokenServiceNftSignDto));
        Result<HashMap> nftSignatureResult = remoteTokenService.getNFTSignature(tokenServiceNftSignDto);
        log.info("nftSignatureResult={}",JSON.toJSONString(nftSignatureResult));
        return nftSignatureResult;
    }
}
