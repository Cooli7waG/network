package com.aitos.xenon.account.api.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PoggRewardDetailDto {

    private String address;

    /**
     *  owner
     */
    private String ownerAddress;

    /**
     * 奖励数量
     */
    private BigDecimal amount;
}
