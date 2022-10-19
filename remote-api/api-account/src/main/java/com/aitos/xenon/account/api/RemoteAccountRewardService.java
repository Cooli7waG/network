package com.aitos.xenon.account.api;


import com.aitos.xenon.account.api.domain.vo.AccountRewardStatisticsVo;
import com.aitos.xenon.core.model.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "account")
public interface RemoteAccountRewardService {

    @GetMapping("/accountreward/statistics")
    Result<AccountRewardStatisticsVo> statistics(@RequestParam("address") String address);

}
