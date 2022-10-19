package com.aitos.xenon.account.service.impl;

import com.aitos.xenon.account.api.domain.dto.AccountRewardSearchDto;
import com.aitos.xenon.account.api.domain.vo.*;
import com.aitos.xenon.account.domain.AccountReward;
import com.aitos.xenon.account.mapper.AccountRewardMapper;
import com.aitos.xenon.account.service.AccountRewardService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    @Override
    public IPage<AccountRewardVo> findListByPage(AccountRewardSearchDto queryParams) {
        Page<AccountRewardVo> page = new Page<AccountRewardVo>(queryParams.getOffset(), queryParams.getLimit());
        IPage<AccountRewardVo> pageResult = accountRewardMapper.findListByPage(page, queryParams);
        return pageResult;
    }

    @Override
    public List<AccountRewardStatisticsDayVo> statisticsRewardByDay(String address, LocalDateTime startTime, LocalDateTime endTime) {
        return accountRewardMapper.statisticsRewardByDay(address,startTime,endTime);
    }

    @Override
    public List<AccountRewardStatisticsByOwnerVo> statisticsRewardsByOwnerAddress(String ownerAddress, LocalDateTime startTime, LocalDateTime endTime) {
        return accountRewardMapper.statisticsRewardsByOwnerAddress(ownerAddress,startTime,endTime);
    }


}
