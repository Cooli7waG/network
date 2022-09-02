package com.aitos.xenon.account.service.impl;

import com.aitos.xenon.account.api.domain.vo.AccountRewardStatisticsVo;
import com.aitos.xenon.account.domain.AccountReward;
import com.aitos.xenon.account.mapper.AccountRewardMapper;
import com.aitos.xenon.account.service.AccountRewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccountRewardServiceImpl implements AccountRewardService {

    @Autowired
    private AccountRewardMapper accountRewardMapper;

    @Override
    public void batchSave(List<AccountReward> accountRewardList) {
        accountRewardMapper.batchSave(accountRewardList);
    }

    @Override
    public AccountRewardStatisticsVo statisticsTotalRewards(String address) {
        return accountRewardMapper.statisticsTotalRewards(address);
    }

    @Override
    public BigDecimal statisticsRewards(String address, LocalDateTime startTime, LocalDateTime endTime) {
        return accountRewardMapper.statisticsRewards(address,startTime,endTime);
    }


}
