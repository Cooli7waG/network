package com.aitos.xenon.device.controller;

import com.aitos.xenon.core.constant.ApiStatus;
import com.aitos.xenon.core.model.Page;
import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.device.api.domain.dto.DeviceSearchDto;
import com.aitos.xenon.device.api.domain.vo.DeviceVo;
import com.aitos.xenon.device.service.DeviceService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 专用于console的接口
 * @author xymoc
 */
@RestController
@RequestMapping("/owner/device")
@Slf4j
@RefreshScope
public class OwnerDeviceController {

    @Autowired
    private DeviceService deviceService;

    /**
     * 分页查询miner信息接口
     * @param queryParams
     * @return
     */
    @GetMapping("/list")
    public Result<Page<DeviceVo>> list(DeviceSearchDto queryParams){
        if(!StringUtils.hasText(queryParams.getOwner())){
            return Result.failed(ApiStatus.PARAMETER_FORMATE_ERROR.getMsg());
        }
        IPage<DeviceVo> listPage= deviceService.list(queryParams);
        Page<DeviceVo> deviceVoPage=new Page<DeviceVo>(listPage.getTotal(),listPage.getRecords());
        return Result.ok(deviceVoPage);
    }
}
