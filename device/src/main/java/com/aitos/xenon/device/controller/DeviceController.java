package com.aitos.xenon.device.controller;

import com.aitos.xenon.common.crypto.ed25519.Ed25519;
import com.aitos.xenon.core.constant.ApiStatus;
import com.aitos.xenon.core.model.Page;
import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.core.utils.BeanConvertor;
import com.aitos.xenon.device.api.domain.dto.DeviceBindDto;
import com.aitos.xenon.device.api.domain.dto.DeviceDetialDto;
import com.aitos.xenon.device.api.domain.dto.DeviceRegisterDto;
import com.aitos.xenon.device.api.domain.dto.DeviceSearchDto;
import com.aitos.xenon.device.api.domain.vo.DeviceVo;
import com.aitos.xenon.device.domain.Device;
import com.aitos.xenon.device.domain.DeviceDetial;
import com.aitos.xenon.device.service.DeviceDetialService;
import com.aitos.xenon.device.service.DeviceService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/device")
@Slf4j
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private DeviceDetialService deviceDetialService;

    @Value("${foundation.publicKey}")
    private String foundationPublicKey;

    @PostMapping("/register")
    public Result register(@RequestBody DeviceRegisterDto deviceRegister){

        Boolean verify= Ed25519.verifyBase58(foundationPublicKey,deviceRegister.getAddress(),deviceRegister.getSignature());
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
        String ownerAddress=paramsObject.getString("ownerAddress");
        String ownerSignature=paramsObject.getString("ownerSignature");
        String minerAddress=paramsObject.getString("minerAddress");
        String minerSignature=paramsObject.getString("minerSignature");

        paramsObject.remove("payerSignature");
        String payerData=paramsObject.toJSONString();
        log.info("onboard.payerData:{}",payerData);
        paramsObject.remove("ownerSignature");
        String ownerData=paramsObject.toJSONString();
        log.info("onboard.ownerData:{}",ownerData);
        paramsObject.remove("minerSignature");
        String minerData=paramsObject.toJSONString();
        log.info("onboard.minerData:{}",minerData);

        if(!Ed25519.verifyBase58(payerAddress,payerData,payerSignature)){
            return Result.failed(ApiStatus.BUSINESS_DEVICE_PAYER_SIGN_ERROR);
        }
        if(!Ed25519.verifyBase58(ownerAddress,ownerData,ownerSignature)){
            return Result.failed(ApiStatus.BUSINESS_DEVICE_OWNER_SIGN_ERROR);
        }
        if(!Ed25519.verifyBase58(minerAddress,minerData,minerSignature)){
            return Result.failed(ApiStatus.BUSINESS_DEVICE_MINER_SIGN_ERROR);
        }

        DeviceBindDto deviceBindDto= JSON.parseObject(params,DeviceBindDto.class);
        deviceBindDto.setTxData(params);
        String txHash= DigestUtils.sha256Hex(deviceBindDto.getTxData());
        deviceBindDto.setTxHash(txHash);

        Device deviceTemp=deviceService.findByAddress(deviceBindDto.getMinerAddress());
        if(deviceTemp==null){
            return Result.failed(ApiStatus.BUSINESS_DEVICE_NOT_EXISTED);
        }else if(deviceTemp!=null&&deviceTemp.getAccountId()!=null){
            return Result.failed(ApiStatus.BUSINESS_DEVICE_BOUND);
        }
        deviceBindDto.setDeviceId(deviceTemp.getId());


        deviceService.bind(deviceBindDto);

        return Result.ok(txHash);
    }

    @GetMapping("/queryByMiner")
    public Result<DeviceVo> queryByMiner(@RequestParam("minerAddress")String minerAddress){
       DeviceVo device= deviceService.queryByMiner(minerAddress);
        return Result.ok(device);
    }

    @GetMapping("/queryByOwner")
    public Result<List<DeviceVo>> queryByOwner(@RequestParam("ownerAddress") String ownerAddress){
        List<DeviceVo> list=deviceService.queryByOwner(ownerAddress);
        List<DeviceVo> deviceDetialVo=BeanConvertor.toList(list,DeviceVo.class);
        return Result.ok(deviceDetialVo);
    }

    @GetMapping("/list")
    public Result<Page<DeviceVo>> list(DeviceSearchDto queryParams){
        IPage<Device> listPage= deviceService.list(queryParams);

        List<DeviceVo> deviceVoList=BeanConvertor.toList(listPage.getRecords(),DeviceVo.class);
        Page<DeviceVo> deviceVoPage=new Page<DeviceVo>(listPage.getTotal(),deviceVoList);
        return Result.ok(deviceVoPage);
    }

    @PutMapping("/detial")
    public Result updateDeviceDetial(@RequestBody DeviceDetialDto deviceDetialDto){
        DeviceDetial  deviceDetial=BeanConvertor.toBean(deviceDetialDto,DeviceDetial.class);
        Device device=deviceService.findByAddress(deviceDetialDto.getAddress());
        deviceDetial.setDeviceId(device.getId());
        log.info("updateDeviceDetial:",JSON.toJSONString(deviceDetial));
        deviceDetialService.updateDeviceDetial(deviceDetial);
        return Result.ok();
    }


}
