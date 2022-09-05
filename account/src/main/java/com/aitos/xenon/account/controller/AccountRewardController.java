package com.aitos.xenon.account.controller;

import com.aitos.xenon.account.api.domain.dto.AccountRewardSearchDto;
import com.aitos.xenon.account.api.domain.vo.AccountRewardStatisticsVo;
import com.aitos.xenon.account.api.domain.vo.AccountRewardVo;
import com.aitos.xenon.account.service.AccountRewardService;
import com.aitos.xenon.core.model.Page;
import com.aitos.xenon.core.model.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        accountRewardStatisticsVo.setTodayReward(todayReward);

        LocalDateTime yesterdayStart=LocalDateTime.now().minusDays(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime yesterdayEnd=LocalDateTime.now().minusDays(1).withHour(23).withMinute(59).withSecond(59);
        BigDecimal yesterdayReward = accountRewardService.statisticsRewards(address, yesterdayStart, yesterdayEnd);
        accountRewardStatisticsVo.setYesterdayReward(yesterdayReward);
        accountRewardStatisticsVo.setAddress(address);
        return Result.ok(accountRewardStatisticsVo);
    }

    @PostMapping("/list")
    public Result<Page<AccountRewardVo>> list(@RequestBody AccountRewardSearchDto queryParams){
        IPage<AccountRewardVo> listPage= accountRewardService.findListByPage(queryParams);
        Page<AccountRewardVo> poggRewardMinerPage=new Page<AccountRewardVo>(listPage.getTotal(),listPage.getRecords());
        return Result.ok(poggRewardMinerPage);
    }
}
