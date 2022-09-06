package com.aitos.xenon.device.controller;

import com.aitos.common.crypto.coder.DataCoder;
import com.aitos.common.crypto.ecdsa.Ecdsa;
import com.aitos.xenon.core.constant.ApiStatus;
import com.aitos.xenon.core.model.Page;
import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.core.utils.BeanConvertor;
import com.aitos.xenon.device.api.domain.dto.*;
import com.aitos.xenon.device.api.domain.vo.DeviceVo;
import com.aitos.xenon.device.domain.Device;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/device")
@Slf4j
@RefreshScope
public class DeviceController {

    @Autowired
    private DeviceService deviceService;
    @Value("${foundation.publicKey}")
    private String foundationPublicKey;
    @Value("${xenon.airdrop.apply.active}")
    private Boolean applyActive;
    @Value("${xenon.airdrop.apply.personalSign}")
    private Boolean applyPersonalSign;

    @PostMapping("/register")
    public Result register(@RequestBody DeviceRegisterDto deviceRegister){
        //TODO 此处不能只验证一个字段
        log.info("DeviceController.register address:{}",deviceRegister.getAddress());
        log.info("DeviceController.register FoundationSignature:{}",deviceRegister.getFoundationSignature());
        log.info("DeviceController.register foundationPublicKey:{}",foundationPublicKey);
        Boolean verify= Ecdsa.verifyByAddress(foundationPublicKey,deviceRegister.getAddress(),deviceRegister.getFoundationSignature(), DataCoder.BASE58);
        //JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(deviceRegister));
        //jsonObject.remove("foundationSignature");
        //String jsonData=jsonObject.toJSONString();
        //log.info("device register:{}",jsonData);
        //Boolean verify= XenonCrypto.verify(foundationPublicKey,jsonData.getBytes(StandardCharsets.UTF_8),deviceRegister.getFoundationSignature().getBytes(StandardCharsets.UTF_8));
        if(!verify){
            return Result.failed(ApiStatus.VALIDATE_SIGN_FAILED);
        }
        Device deviceTemp=deviceService.findByAddress(deviceRegister.getAddress());
        if(deviceTemp!=null){
            return Result.failed(ApiStatus.BUSINESS_DEVICE_ID_EXISTED);
        }

        deviceRegister.setTxData(JSON.toJSONString(deviceRegister));

        deviceService.save(deviceRegister);
        return Result.ok();
    }

    @PostMapping("/onboard")
    public Result onboard(@RequestBody String params){
        log.info("onboard.params:{}",params);

        JSONObject paramsObject = JSONObject.parseObject(params, Feature.OrderedField);

        String payerAddress=paramsObject.getString("payerAddress");
        String payerSignature=paramsObject.getString("payerSignature");
        /*String ownerAddress=paramsObject.getString("ownerAddress");
        String ownerSignature=paramsObject.getString("ownerSignature");*/
        String minerAddress=paramsObject.getString("minerAddress");
        String minerSignature=paramsObject.getString("minerSignature");

        paramsObject.remove("payerSignature");
        String payerData=paramsObject.toJSONString();
        log.info("onboard.payerData:{}",payerData);
        paramsObject.remove("ownerSignature");
        /*String ownerData=paramsObject.toJSONString();
        log.info("onboard.ownerData:{}",ownerData);*/
        paramsObject.remove("minerSignature");
        String minerData=paramsObject.toJSONString();
        log.info("onboard.minerData:{}",minerData);

        if(!Ecdsa.verifyByAddress(payerAddress,payerData.getBytes(),payerSignature, DataCoder.BASE58)){
            return Result.failed(ApiStatus.BUSINESS_DEVICE_PAYER_SIGN_ERROR);
        }
        /*if(!XenonCrypto.verify(ownerAddress,ownerData,ownerSignature)){
            return Result.failed(ApiStatus.BUSINESS_DEVICE_OWNER_SIGN_ERROR);
        }*/
        if(!Ecdsa.verifyByAddress(minerAddress,minerData.getBytes(),minerSignature, DataCoder.BASE58)){
            return Result.failed(ApiStatus.BUSINESS_DEVICE_MINER_SIGN_ERROR);
        }

        DeviceBindDto deviceBindDto= JSON.parseObject(params,DeviceBindDto.class);
        deviceBindDto.setTxData(params);
        String txHash= DigestUtils.sha256Hex(deviceBindDto.getTxData());
        deviceBindDto.setTxHash(txHash);

        Device deviceTemp=deviceService.findByAddress(deviceBindDto.getMinerAddress());
        if(deviceTemp==null){
            return Result.failed(ApiStatus.BUSINESS_DEVICE_NOT_EXISTED);
        }else if(deviceTemp!=null&& StringUtils.hasText(deviceTemp.getOwnerAddress())){
            return Result.failed(ApiStatus.BUSINESS_DEVICE_BOUND);
        }
        deviceBindDto.setDeviceId(deviceTemp.getId());

        deviceService.bind(deviceBindDto);

        return Result.ok(txHash);
    }

    @PostMapping("/terminate")
    public Result terminate(@RequestBody DeviceTerminateMinerDto deviceTerminateMinerDto){
        log.info("terminate.params:{}",JSON.toJSONString(deviceTerminateMinerDto));

        if(!Ecdsa.verifyByAddress(foundationPublicKey,deviceTerminateMinerDto.getAddress(),deviceTerminateMinerDto.getFoundationSignature(), DataCoder.BASE58)){
            return Result.failed(ApiStatus.BUSINESS_FOUNDATION_SIGN_ERROR);
        }
        deviceService.terminate(deviceTerminateMinerDto);
        return Result.ok();
    }

    @GetMapping("/queryByMiner")
    public Result<DeviceVo> queryByMiner(@RequestParam("minerAddress")String minerAddress){
        DeviceVo deviceVo= deviceService.queryByMiner(minerAddress);
        return Result.ok(deviceVo);
    }

    @GetMapping("/list")
    public Result<Page<DeviceVo>> list(DeviceSearchDto queryParams){
        IPage<DeviceVo> listPage= deviceService.list(queryParams);
        Page<DeviceVo> deviceVoPage=new Page<DeviceVo>(listPage.getTotal(),listPage.getRecords());
        return Result.ok(deviceVoPage);
    }

    @PutMapping("/update")
    public Result update(@RequestBody DeviceDto deviceDto){
        Device device=BeanConvertor.toBean(deviceDto,Device.class);
        deviceService.update(device);
        return Result.ok();
    }

    @PostMapping("/getMinersByOwnerAddress")
    public Result<Page<DeviceVo>> getMinersByOwnerAddress(@RequestBody DeviceSearchDto deviceSearchDto){
        log.info("getMinersByOwnerAddress:{}",JSON.toJSONString(deviceSearchDto));
        IPage<DeviceVo> listPage= deviceService.getMinersByOwnerAddress(deviceSearchDto);
        Page<DeviceVo> deviceVoPage=new Page<DeviceVo>(listPage.getTotal(),listPage.getRecords());
        return Result.ok(deviceVoPage);
    }

    /**
     * 获取所有miner位置信息
     * @return
     */
    @PostMapping("/getMinerLocation")
    public Result<HashMap> getMinerLocation(){
        HashMap map = deviceService.getMinerLocation();
        return Result.ok(map);
    }

    @PostMapping("/apply")
    public Result applyGameMiner(@RequestBody String applyGameMiner){
        if(!applyActive){
            return Result.failed(ApiStatus.BUSINESS_AIRDROP_NOT_ACTIVE.getMsg());
        }
        log.info("applyGameMiner:{}",applyGameMiner);
        String result = deviceService.applyGameMiner(applyGameMiner);
        return Result.ok(result);
    }

    @PostMapping("/claim")
    public Result claimGameMiner(@RequestBody String claimGameMiner){
        log.info("DeviceController.claimGameMiner:{}",claimGameMiner);
        String result = deviceService.claimGameMiner(claimGameMiner);
        return Result.ok(result);
    }

    @PostMapping("/applyAirdrop")
    public Result getApplyActiveInfo(){
        HashMap<String,Boolean> map=new HashMap();
        map.put("applyActive",applyActive);
        map.put("applyPersonalSign",applyPersonalSign);
        return Result.ok(map);
    }

    @PostMapping("/loadMinersInfo")
    public Result loadMinersInfo(@RequestBody(required = false) ArrayList<String> addressList){
        log.info("loadMinersInfo:{}",JSON.toJSONString(addressList));
        if(addressList==null||addressList.size()<=0){
            return Result.failed();
        }
        List<DeviceVo> deviceList = deviceService.loadMinersInfo(addressList);
        return Result.ok(deviceList);
    }

    @GetMapping("/getMinerListByOwner/{address}")
    public Result getMinerListByOwner(@PathVariable("address") String address){
        List<DeviceVo> deviceList = deviceService.getMinerListByOwner(address);
        return Result.ok(deviceList);
    }
}
