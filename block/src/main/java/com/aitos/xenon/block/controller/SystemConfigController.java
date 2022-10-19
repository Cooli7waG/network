package com.aitos.xenon.block.controller;


import com.aitos.xenon.block.api.domain.vo.SystemConfigVo;
import com.aitos.xenon.block.domain.SystemConfig;
import com.aitos.xenon.block.service.SystemConfigService;
import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.core.utils.BeanConvertor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/systemconfig")
public class SystemConfigController {

    @Autowired
    private SystemConfigService systemConfigService;

    @GetMapping("/findConfig")
    public Result<SystemConfigVo> findConfig(){
        SystemConfig config = systemConfigService.findConfig(true);
        SystemConfigVo  systemConfigVo= BeanConvertor.toBean(config,SystemConfigVo.class);
        return Result.ok(systemConfigVo);
    }
}
