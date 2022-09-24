package com.aitos.xenon.account.domain;

import com.aitos.xenon.core.model.BaseModel;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
public class Account extends BaseModel {

    private String address;

    private String balance;

    private Long nonce;

    /**
     * 1.owner账户；2miner账户
     */
    private Integer  accountType;

    /**
     * 总挖矿收入
     */
    private BigDecimal earningMint;
    /**
     * 总服务收入
     */
    private BigDecimal earningService;

    /**
     * 总体现数量
     */
    private BigDecimal withdrawAmount;
}
