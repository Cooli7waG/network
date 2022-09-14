package com.aitos.xenon.account.api.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AccountRewardStatisticsVo {
    private String address;
    /**
     * 总奖励
     */
    private BigDecimal totalReward;
    /**
     * 平均奖励
     */
    private BigDecimal avgReward;
}
