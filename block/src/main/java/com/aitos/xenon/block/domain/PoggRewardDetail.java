package com.aitos.xenon.block.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PoggRewardDetail {

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
