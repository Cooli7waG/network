package com.aitos.xenon.block.controller;


import com.aitos.common.crypto.coder.DataCoder;
import com.aitos.common.crypto.ecdsa.Ecdsa;
import com.aitos.xenon.block.api.domain.dto.PoggGreenDataDto;
import com.aitos.xenon.block.api.domain.dto.PoggReportDto;
import com.aitos.xenon.block.api.domain.dto.PoggReportSearchDto;
import com.aitos.xenon.block.api.domain.vo.PoggReportDataVo;
import com.aitos.xenon.block.domain.PoggCommit;
import com.aitos.xenon.block.service.PoggReportService;
import com.aitos.xenon.block.service.PoggService;
import com.aitos.xenon.core.constant.ApiStatus;
import com.aitos.xenon.core.model.Page;
import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.core.utils.ValidatorUtils;
import com.aitos.xenon.device.api.RemoteDeviceService;
import com.aitos.xenon.device.api.domain.vo.DeviceVo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/pogg")
public class PoggController {

    @Autowired
    private PoggService poggService;

    @Autowired
    private RemoteDeviceService remoteDeviceService;

    @Autowired
    private PoggReportService poggReportService;

    @Value("${pogg.challengeHit}")
    private Integer challengeHit;

    @PostMapping("/report")
    public Result<String> report(@RequestBody String body){

        JSONObject paramsObject = JSONObject.parseObject(body, Feature.OrderedField);

        String address= paramsObject.getString("address");
        String signature=paramsObject.getString("signature");

        paramsObject.remove("signature");
        String data=paramsObject.toJSONString();

        //验证签名
        if(!Ecdsa.verifyByAddress(address,data,signature, DataCoder.BASE58)){
            return Result.failed(ApiStatus.BUSINESS_POGG_REPORT_SIGN_ERROR);
        }

        //验证参数格式
        PoggReportDto poggReportDto=JSON.parseObject(body,PoggReportDto.class);
        ValidatorUtils.validateFast(poggReportDto);


        //验证设备是否合法
        Result<DeviceVo>  deviceVoResult= remoteDeviceService.queryByMiner(poggReportDto.getAddress());
        if(deviceVoResult.getCode()==ApiStatus.SUCCESS.getCode()&& deviceVoResult.getData()!=null){
            DeviceVo deviceVo= deviceVoResult.getData();
            if(!StringUtils.hasText(deviceVo.getOwnerAddress())){
                return Result.failed(ApiStatus.BUSINESS_DEVICE_NO_ONBOARD);
            }else if(deviceVo.getTerminate()==1){
                return Result.failed(ApiStatus.BUSINESS_DEVICE_TERMINATE);
            }else{
                //设置miner类型
                poggReportDto.setMinerType(deviceVo.getMinerType());
                poggReportDto.setOwnerAddress(deviceVo.getOwnerAddress());
            }
        }else{
            return Result.failed(ApiStatus.BUSINESS_DEVICE_NOT_EXISTED);
        }

        List<PoggGreenDataDto> poggGreenDataDtoList = PoggGreenDataDto.decode(poggReportDto.getDataList());

        poggReportDto.setRawDataJSON(body);
        poggReportDto.setGreenDataList(poggGreenDataDtoList);

        PoggCommit currentPoggCommit=poggService.findCurrentCommit();
        poggReportDto.setEpoch(currentPoggCommit.getEpoch());
        //TODO
        String txHash=poggReportService.reportSave(poggReportDto);
        return Result.ok(txHash);
    }

    @PostMapping("/reportDataList")
    public Result<Page<PoggReportDataVo>> reportDataList(@RequestBody PoggReportSearchDto queryParams){
        log.info("reportDataList.params:{}",JSON.toJSONString(queryParams));
        IPage<PoggReportDataVo> listPage= poggReportService.findReportDataListByPage(queryParams);
        Page<PoggReportDataVo> poggReportPage=new Page<PoggReportDataVo>(listPage.getTotal(),listPage.getRecords());
        return Result.ok(poggReportPage);
    }

    @GetMapping("/report/avgPower")
    public Result<Double> avgPower(String ownerAddress){
        double avgPower = poggReportService.avgPower(ownerAddress);
        return Result.ok(avgPower);
    }
}
