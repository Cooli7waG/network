package com.aitos.xenon.account.controller;

import com.aitos.xenon.account.api.domain.dto.AccountRewardSearchDto;
import com.aitos.xenon.account.api.domain.dto.AccountRewardStatisticsDayDto;
import com.aitos.xenon.account.api.domain.dto.AccountRewardStatisticsDto;
import com.aitos.xenon.account.api.domain.vo.AccountRewardStatisticsByOwnerVo;
import com.aitos.xenon.account.api.domain.vo.AccountRewardStatisticsDayVo;
import com.aitos.xenon.account.api.domain.vo.AccountRewardStatisticsVo;
import com.aitos.xenon.account.api.domain.vo.AccountRewardVo;
import com.aitos.xenon.account.service.AccountRewardService;
import com.aitos.xenon.core.constant.ApiStatus;
import com.aitos.xenon.core.model.Page;
import com.aitos.xenon.core.model.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/accountreward")
public class AccountRewardController {

    @Autowired
    private AccountRewardService accountRewardService;

    @GetMapping("/statistics")
    public Result<AccountRewardStatisticsVo> statistics(@RequestParam String address){
        AccountRewardStatisticsVo  accountRewardStatisticsVo = accountRewardService.statisticsTotalRewards(address);
        return Result.ok(accountRewardStatisticsVo);
    }

    @PostMapping("/statisticsByDateTimeRange")
    public Result<BigDecimal> statisticsByDateTimeRange(@RequestBody AccountRewardStatisticsDto accountRewardStatisticsDto){
        BigDecimal todayReward = accountRewardService.statisticsRewards(accountRewardStatisticsDto.getAddress(), accountRewardStatisticsDto.getStartTime(), accountRewardStatisticsDto.getEndTime());
        return Result.ok(todayReward);
    }

    @PostMapping("/list")
    public Result<Page<AccountRewardVo>> list(@RequestBody AccountRewardSearchDto queryParams){
        IPage<AccountRewardVo> listPage= accountRewardService.findListByPage(queryParams);
        Page<AccountRewardVo> poggRewardMinerPage=new Page<AccountRewardVo>(listPage.getTotal(),listPage.getRecords());
        return Result.ok(poggRewardMinerPage);
    }

    @PostMapping("/statisticsRewardsByOwnerAddress")
    public Result<List<AccountRewardStatisticsByOwnerVo>> statisticsRewardsByOwnerAddress(@RequestBody AccountRewardStatisticsDto accountRewardStatisticsDto){
        List<AccountRewardStatisticsByOwnerVo> list=accountRewardService.statisticsRewardsByOwnerAddress(accountRewardStatisticsDto.getOwnerAddress(), accountRewardStatisticsDto.getStartTime(), accountRewardStatisticsDto.getEndTime());
        return Result.ok(list);
    }

    /**
     * 按天统计每天的奖励数
     * @param queryParams
     * @return
     */
    @PostMapping("/statisticsRewardByDay")
    public Result<List<AccountRewardStatisticsDayVo>> statisticsRewardByDay(@Validated @RequestBody AccountRewardStatisticsDayDto queryParams){
        List<AccountRewardStatisticsDayVo> list= accountRewardService.statisticsRewardByDay(queryParams.getAddress(),queryParams.getStartTime(),queryParams.getEndTime());
        List<AccountRewardStatisticsDayVo> accountRewardStatisticsDayVoList = convertorDate(queryParams.getStartTime().toLocalDate(), queryParams.getEndTime().toLocalDate(), list);
        return Result.ok(accountRewardStatisticsDayVoList);
    }

    private List<AccountRewardStatisticsDayVo> convertorDate(LocalDate startTime, LocalDate endTime, List<AccountRewardStatisticsDayVo> list){
        List<AccountRewardStatisticsDayVo>  newList=new ArrayList<>();
        while (startTime.isBefore(endTime)||startTime.isEqual(endTime)){
            AccountRewardStatisticsDayVo accountRewardStatisticsDayVo=new AccountRewardStatisticsDayVo();
            accountRewardStatisticsDayVo.setDataDate(startTime);
            accountRewardStatisticsDayVo.setReward(new BigDecimal("0"));
            LocalDate finalStartTime = startTime;
            AccountRewardStatisticsDayVo accountRewardStatisticsDayVoTemp = list.stream()
                    .filter(item -> item.getDataDate().isEqual(finalStartTime)).findAny().orElse(accountRewardStatisticsDayVo);
            newList.add(accountRewardStatisticsDayVoTemp);
            startTime=startTime.plusDays(1);
        }
        return newList;
    }
}
