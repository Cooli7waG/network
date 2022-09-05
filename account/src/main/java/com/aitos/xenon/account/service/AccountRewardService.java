package com.aitos.xenon.account.service;

import com.aitos.xenon.account.api.domain.dto.AccountRewardSearchDto;
import com.aitos.xenon.account.api.domain.vo.AccountRewardStatisticsVo;
import com.aitos.xenon.account.api.domain.vo.AccountRewardVo;
import com.aitos.xenon.account.domain.AccountReward;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface AccountRewardService {

    void batchSave(List<AccountReward> accountRewardList);

    AccountRewardStatisticsVo statisticsTotalRewards(@Param("address") String address);
    /**
     * 统计时间范围内的奖励总数
     * @param address
     * @param startTime
     * @param endTime
     * @return
     */
    BigDecimal statisticsRewards(String address,LocalDateTime startTime,LocalDateTime endTime);

    /**
     * 分页查询奖励记录
     * @param queryParams
     * @return
     */
    IPage<AccountRewardVo> findListByPage(AccountRewardSearchDto queryParams);
}
