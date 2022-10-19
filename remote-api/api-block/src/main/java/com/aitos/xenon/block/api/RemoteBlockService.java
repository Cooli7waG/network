package com.aitos.xenon.block.api;


import com.aitos.xenon.core.model.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "block")
public interface RemoteBlockService {

    @GetMapping("/block/height")
    Result<Long> getBlockHeight();

}
