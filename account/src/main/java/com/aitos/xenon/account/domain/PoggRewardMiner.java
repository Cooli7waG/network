package com.aitos.xenon.account.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PoggRewardMiner {

    private Long id;

    private String address;

    /**
     *  owner address
     */
    private String ownerAddress;

    /**
     * 奖励数量
     */
    private BigDecimal amount;

    private Date createTime;
    /**
     * 交易hash
     */
    private String hash;
    /**
     * 区块高度
     */
    private Long height;
    /**
     * 获奖百分比
     */
    private String rewardPercent;
}
