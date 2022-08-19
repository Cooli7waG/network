package com.aitos.xenon.device.controller;

import com.aitos.xenon.block.api.RemoteBlockService;
import com.aitos.xenon.common.crypto.XenonCrypto;
import com.aitos.xenon.core.constant.ApiStatus;
import com.aitos.xenon.core.constant.BusinessConstants;
import com.aitos.xenon.core.model.Page;
import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.core.utils.BeanConvertor;
import com.aitos.xenon.device.api.domain.dto.*;
import com.aitos.xenon.device.api.domain.vo.DeviceVo;
import com.aitos.xenon.device.domain.AirDropRecord;
import com.aitos.xenon.device.domain.Device;
import com.aitos.xenon.device.service.AirDropRecordService;
import com.aitos.xenon.device.service.DeviceService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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

    @Value("${foundation.publicKey}")
    private String foundationPublicKey;

    @PostMapping("/airdrop")
    public Result airDrop(@RequestBody String body){
        log.info("claim.body:{}",body);

        JSONObject jsonObject=JSONObject.parseObject(body, Feature.OrderedField);
        String signature=jsonObject.getString("signature");
        jsonObject.remove("signature");
        String jsonData=jsonObject.toJSONString();

        AirDropDto airDropDto=JSON.parseObject(body,AirDropDto.class);
        Boolean verify= XenonCrypto.verify(foundationPublicKey,jsonData.getBytes(),signature);
        if(!verify){
            return Result.failed(ApiStatus.VALIDATE_SIGN_FAILED);
        }
        //检查设备状态
        Device device = deviceService.findByAddress(airDropDto.getMinerAddress());
        if(device==null){
            return Result.failed(ApiStatus.BUSINESS_DEVICE_NOT_EXISTED);
        }else if(StringUtils.hasText(device.getOwnerAddress())){
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
        return Result.ok();
    }


    @PostMapping("/claim")
    public Result claim(@RequestBody String body){
        log.info("claim.body:{}",body);

        JSONObject jsonObject=JSONObject.parseObject(body, Feature.OrderedField);
        String signature=jsonObject.getString("signature");
        jsonObject.remove("signature");
        String jsonData=jsonObject.toJSONString();

        ClaimDto claimDto=JSON.parseObject(body,ClaimDto.class);
        Boolean verify= XenonCrypto.verify(claimDto.getOwnerAddress(),jsonData.getBytes(),signature);
        if(!verify){
            return Result.failed(ApiStatus.VALIDATE_SIGN_FAILED);
        }
        //检查设备状态
        Device device = deviceService.findByAddress(claimDto.getMinerAddress());
        if(device==null){
            return Result.failed(ApiStatus.BUSINESS_DEVICE_NOT_EXISTED);
        }else if(StringUtils.hasText(device.getOwnerAddress())){
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

}
