package com.aitos.xenon.account.controller;

import com.aitos.xenon.account.api.domain.vo.AccountRewardStatisticsVo;
import com.aitos.xenon.account.service.AccountRewardService;
import com.aitos.xenon.core.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@RestController
@RequestMapping("/accountreward")
public class AccountRewardController {

    @Autowired
    private AccountRewardService accountRewardService;

    @GetMapping("/statistics")
    public Result<AccountRewardStatisticsVo> statistics(@RequestParam String address){
        AccountRewardStatisticsVo  accountRewardStatisticsVo = accountRewardService.statisticsTotalRewards(address);

        LocalDateTime todayStart=LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime todayEnd=LocalDateTime.now();
        BigDecimal todayReward = accountRewardService.statisticsRewards(address, todayStart, todayEnd);
        accountRewardStatisticsVo.setTotalReward(todayReward);

        LocalDateTime yesterdayStart=LocalDateTime.now().minusDays(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime yesterdayEnd=LocalDateTime.now().minusDays(1).withHour(23).withMinute(59).withSecond(59);
        BigDecimal yesterdayReward = accountRewardService.statisticsRewards(address, yesterdayStart, yesterdayEnd);
        accountRewardStatisticsVo.setYesterdayReward(yesterdayReward);
        accountRewardStatisticsVo.setAddress(address);
        return Result.ok(accountRewardStatisticsVo);
    }

}
