package com.aitos.xenon.account.api.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 根据owner统计奖励
 */
@Data
public class AccountRewardStatisticsByOwnerVo {
    private String address;
    /**
     * 奖励
     */
    private BigDecimal reward;
}
