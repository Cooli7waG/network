package com.aitos.xenon.device.controller;

import com.aitos.common.crypto.coder.DataCoder;
import com.aitos.common.crypto.ecdsa.Ecdsa;
import com.aitos.xenon.core.constant.ApiStatus;
import com.aitos.xenon.core.constant.BusinessConstants;
import com.aitos.xenon.core.model.Page;
import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.core.utils.BeanConvertor;
import com.aitos.xenon.device.api.domain.dto.*;
import com.aitos.xenon.device.api.domain.vo.DeviceVo;
import com.aitos.xenon.device.domain.Device;
import com.aitos.xenon.device.service.DeviceService;
import com.aitos.xenon.fundation.api.RemoteMakerService;
import com.aitos.xenon.fundation.api.domain.vo.MakerVo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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

    @Autowired
    private RemoteMakerService remoteMakerService;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 设备注册接口
     * @param deviceRegister
     * @return
     */
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

    /**
     * 设备绑定接口
     * @param params
     * @return
     */
    @PostMapping("/onboard")
    public Result onboard(@RequestBody String params){
        log.info("onboard.params:{}",params);

        JSONObject paramsObject = JSONObject.parseObject(params, Feature.OrderedField);

        String payerAddress=paramsObject.getString("payerAddress");
        String payerSignature=paramsObject.getString("payerSignature");
        String minerAddress=paramsObject.getString("minerAddress");
        String minerSignature=paramsObject.getString("minerSignature");

        paramsObject.remove("payerAddress");
        paramsObject.remove("payerSignature");
        String payerData=paramsObject.toJSONString();
        log.info("onboard.payerData:{}",payerData);
        paramsObject.remove("minerSignature");
        String minerData=paramsObject.toJSONString();
        log.info("onboard.minerData:{}",minerData);

        if(!Ecdsa.verifyByAddress(payerAddress,payerData,payerSignature, DataCoder.BASE58)){
            return Result.failed(ApiStatus.BUSINESS_DEVICE_PAYER_SIGN_ERROR);
        }
        if(!Ecdsa.verifyByAddress(minerAddress,minerData,minerSignature, DataCoder.BASE58)){
            return Result.failed(ApiStatus.BUSINESS_DEVICE_MINER_SIGN_ERROR);
        }

        DeviceBindDto deviceBindDto= JSON.parseObject(params,DeviceBindDto.class);
        deviceBindDto.setTxData(params);
        String txHash= DigestUtils.sha256Hex(deviceBindDto.getTxData());
        deviceBindDto.setTxHash(txHash);

        Device deviceTemp=deviceService.findByAddress(deviceBindDto.getMinerAddress());
        if(deviceTemp==null){
            return Result.failed(ApiStatus.BUSINESS_DEVICE_NOT_EXISTED);
        }else if(deviceTemp.getStatus()== BusinessConstants.DeviceStatus.BOUND){
            return Result.failed(ApiStatus.BUSINESS_DEVICE_BOUND);
        }else if(deviceTemp.getStatus()== BusinessConstants.DeviceStatus.TERMINATE){
            return Result.failed(ApiStatus.BUSINESS_DEVICE_TERMINATE);
        }
        deviceBindDto.setDeviceId(deviceTemp.getId());

        deviceService.bind(deviceBindDto);

        return Result.ok(txHash);
    }

    /**
     * 设备终止接口
     * @param deviceTerminateMinerDto
     * @return
     */
    @PostMapping("/terminate")
    public Result terminate(@RequestBody DeviceTerminateMinerDto deviceTerminateMinerDto){
        log.info("terminate.params:{}",JSON.toJSONString(deviceTerminateMinerDto));

        if(!Ecdsa.verifyByAddress(foundationPublicKey,deviceTerminateMinerDto.getAddress(),deviceTerminateMinerDto.getFoundationSignature(), DataCoder.BASE58)){
            return Result.failed(ApiStatus.BUSINESS_FOUNDATION_SIGN_ERROR);
        }
        deviceService.terminate(deviceTerminateMinerDto);
        return Result.ok();
    }

    /**
     * 根据miner address 查询miner 信息
     * @param minerAddress
     * @return
     */
    @GetMapping("/{minerAddress}")
    public Result<DeviceVo> findByAddress(@PathVariable("minerAddress")String minerAddress){
        DeviceVo deviceVo= deviceService.queryByMiner(minerAddress);
        // 去掉
        deviceVo.setAvgPower(null);
        deviceVo.setTotalEnergyGeneration(null);
        return Result.ok(deviceVo);
    }

    /**
     * todo 该接口后续可能会删掉,改用 findByAddress
     * @param minerAddress
     * @return
     */
    @Deprecated
    @GetMapping("/queryByMiner")
    public Result<DeviceVo> queryByMiner(@RequestParam("minerAddress")String minerAddress){
        DeviceVo deviceVo= deviceService.queryByMiner(minerAddress);
        return Result.ok(deviceVo);
    }

    /**
     * 分页查询miner信息接口
     * @param queryParams
     * @return
     */
    @GetMapping("/list")
    public Result<Page<DeviceVo>> list(DeviceSearchDto queryParams){
        IPage<DeviceVo> listPage= deviceService.list(queryParams);
        Page<DeviceVo> deviceVoPage=new Page<DeviceVo>(listPage.getTotal(),listPage.getRecords());
        return Result.ok(deviceVoPage);
    }

    /**
     * 内部调用接口
     * @param deviceDto
     * @return
     */
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

    @PostMapping("/ownerBindApply")
    public Result ownerApplyBind(@Validated @RequestBody OwnerBindApplyDto ownerBindApplyDto){

        Result<MakerVo> makerVoResult = remoteMakerService.findById(ownerBindApplyDto.getMakerId());
        log.info("ownerApplyBind.makerVoResult={}",JSON.toJSONString(makerVoResult));
        if(makerVoResult.getCode()!=ApiStatus.SUCCESS.getCode()){
            return Result.failed(ApiStatus.BUSINESS_DEVICE_MAKER_INFO_ERROR);
        }
        MakerVo makerVo = makerVoResult.getData();
        String serviceUrl = makerVo.getServiceUrl();
        Result result = call3rdPartyMakerService(serviceUrl, ownerBindApplyDto);
        return result;
    }

    private Result call3rdPartyMakerService(String serviceUrl,OwnerBindApplyDto ownerBindApplyDto) {
        String jsonString = JSON.toJSONString(ownerBindApplyDto);
        log.info("call3rdPartyMakerService.params={}",jsonString);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("application/json;UTF-8"));
        HttpEntity<String> strEntity = new HttpEntity<String>(jsonString,headers);
        String resultJSON = restTemplate.postForObject(serviceUrl+"device/bindApply",strEntity,String.class);
        log.info("call3rdPartyMakerService.result={}",resultJSON);
        Result result =JSON.parseObject(resultJSON,new TypeReference<Result>(){});
        return result;
    }
}
