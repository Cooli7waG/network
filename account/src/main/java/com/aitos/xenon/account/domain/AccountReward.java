package com.aitos.xenon.account.domain;

import com.aitos.xenon.core.model.BaseModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AccountReward extends BaseModel {

    private String address;
    /**
     * 账户类型
     */
    private Integer accountType;

    /**
     *  owner address
     */
    private String ownerAddress;
    /**
     * 奖励数量
     */
    private BigDecimal amount;
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
    private BigDecimal rewardPercent;
}
