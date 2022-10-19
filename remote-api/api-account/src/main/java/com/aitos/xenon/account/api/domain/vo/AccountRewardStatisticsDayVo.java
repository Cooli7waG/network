package com.aitos.xenon.account.api.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 按天统计奖励
 */
@Data
public class AccountRewardStatisticsDayVo {
    /**
     * 奖励
     */
    private BigDecimal reward;
    /**
     * 数据日期
     */
    private LocalDate dataDate;
}
