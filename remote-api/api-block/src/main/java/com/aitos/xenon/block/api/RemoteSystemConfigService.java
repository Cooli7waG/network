package com.aitos.xenon.block.api;

import com.aitos.xenon.block.api.domain.vo.SystemConfigVo;
import com.aitos.xenon.core.model.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

@FeignClient(name = "block")
public interface RemoteSystemConfigService {

    @GetMapping("/systemconfig/findConfig")
    Result<SystemConfigVo> findConfig();
}
