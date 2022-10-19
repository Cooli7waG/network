package com.aitos.xenon.block.api;

import com.aitos.xenon.block.api.domain.vo.PoggReportDataVo;
import com.aitos.xenon.core.model.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@FeignClient(name = "block")
public interface RemotePoggService {

    @GetMapping("/pogg/report")
    Result<String> report(@RequestBody String body);

    @PostMapping("/pogg/poggHitPerBlocks")
    Result<HashMap<String,String>> poggHitPerBlocks();

    @GetMapping("/pogg/report/avgPower")
    Result<Double> avgPower(@RequestParam("address") String address);

    @GetMapping("/pogg/lastReport/{address}")
    Result<PoggReportDataVo> lastReport(@PathVariable("address") String address);
}
