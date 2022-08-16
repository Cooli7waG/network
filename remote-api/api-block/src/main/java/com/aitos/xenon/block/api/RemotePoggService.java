package com.aitos.xenon.block.api;


import com.aitos.xenon.block.api.domain.PoggReport;
import com.aitos.xenon.block.api.domain.PoggRewardMiner;
import com.aitos.xenon.block.api.domain.dto.PoggReportDto;
import com.aitos.xenon.core.model.Page;
import com.aitos.xenon.core.model.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;

@FeignClient(name = "block")
public interface RemotePoggService {

    @GetMapping("/pogg/report")
    Result<String> report(@RequestBody String body);

    @PostMapping("/pogg/poggHitPerBlocks")
    Result<HashMap<String,String>> poggHitPerBlocks();

    @PostMapping("/pogg/getReport")
    Result<Page<PoggReport>> getReport(PoggReportDto queryParams);

    @PostMapping("/pogg/getReward")
    Result<Page<PoggRewardMiner>> getReward(PoggReportDto queryParams);
}
