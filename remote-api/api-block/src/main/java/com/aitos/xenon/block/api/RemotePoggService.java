package com.aitos.xenon.block.api;


import com.aitos.xenon.block.api.domain.vo.PoggChallengeVo;
import com.aitos.xenon.core.model.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;

@FeignClient(name = "block")
public interface RemotePoggService {

    @GetMapping("/pogg/activeChallenges")
    Result<List<PoggChallengeVo>> activeChallenges();

    @PostMapping("/pogg/poggHitPerBlocks")
    Result<HashMap<String,String>> poggHitPerBlocks();

    @PostMapping("/pogg/response")
    Result response(@RequestBody String body);

}
