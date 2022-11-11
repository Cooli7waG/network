package com.aitos.xenon.device.controller;

import com.aitos.common.crypto.coder.DataCoder;
import com.aitos.common.crypto.ecdsa.Ecdsa;
import com.aitos.xenon.account.api.RemoteTransactionService;
import com.aitos.xenon.account.api.domain.dto.TransactionDto;
import com.aitos.xenon.block.api.RemoteBlockService;
import com.aitos.xenon.core.constant.ApiStatus;
import com.aitos.xenon.core.constant.BusinessConstants;
import com.aitos.xenon.core.exceptions.ServiceException;
import com.aitos.xenon.core.model.Page;
import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.core.utils.BeanConvertor;
import com.aitos.xenon.core.utils.Location;
import com.aitos.xenon.device.api.domain.dto.*;
import com.aitos.xenon.device.api.domain.vo.DeviceVo;
import com.aitos.xenon.device.domain.Device;
import com.aitos.xenon.device.domain.ReportDeviceInfo;
import com.aitos.xenon.device.service.DeviceService;
import com.aitos.xenon.device.service.ReportDeviceInfoService;
import com.aitos.xenon.fundation.api.RemoteMakerService;
import com.aitos.xenon.fundation.api.domain.vo.MakerVo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
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
import java.util.Map;

@RestController
@RequestMapping("/devicereportinfo/")
@Slf4j
@RefreshScope
public class DeviceReportInfoController {

    @Autowired
    private ReportDeviceInfoService reportDeviceInfoService;

    @Autowired
    private RemoteTransactionService remoteTransactionService;

    @Autowired
    private RemoteBlockService remoteBlockService;

    @PostMapping("/report")
    public Result reportDeviceInfo(@Validated @RequestBody ReportDeviceInfoDto reportDeviceInfoDto){

        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        filter.getExcludes().add("signature");
        filter.getExcludes().add("timestampToLong");
        filter.getExcludes().add("powerToLong");
        String dataJson=JSON.toJSONString(reportDeviceInfoDto,filter);


        if(!Ecdsa.verifyByAddress(reportDeviceInfoDto.getMiner(),dataJson,reportDeviceInfoDto.getSignature(), DataCoder.BASE58)){
            return Result.failed(ApiStatus.DEVICE_REPORT_INFO_SIGN_ERROR);
        }

        ReportDeviceInfo reportDeviceInfo=BeanConvertor.toBean(reportDeviceInfoDto,ReportDeviceInfo.class);
        reportDeviceInfo.setTimestamp(reportDeviceInfoDto.getTimestampToLong());
        reportDeviceInfo.setPower(reportDeviceInfoDto.getPowerToLong());
        reportDeviceInfoService.save(reportDeviceInfo);

        Result<Long> blockVoResult = remoteBlockService.getBlockHeight();
        if (blockVoResult.getCode() != ApiStatus.SUCCESS.getCode()) {
            throw new ServiceException(blockVoResult.getMsg());
        }

        SimplePropertyPreFilter filter2 = new SimplePropertyPreFilter();
        filter2.getExcludes().add("location");
        String txDataJson=JSON.toJSONString(reportDeviceInfoDto,filter2);

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setFromAddress(reportDeviceInfoDto.getMiner());
        transactionDto.setHeight(blockVoResult.getData());
        transactionDto.setData(txDataJson);

        String txHash= DigestUtils.sha256Hex(txDataJson);
        transactionDto.setHash(txHash);
        transactionDto.setTxType(BusinessConstants.TXType.TX_REPORTDEVICEINFO);
        Result<String> transaction = remoteTransactionService.transaction(transactionDto);
        if (transaction.getCode() != ApiStatus.SUCCESS.getCode()) {
            throw new ServiceException(transaction.getMsg());
        }
        return Result.ok();
    }
}
