package com.aitos.xenon.account.api.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
public class AccountVo {

    private Long id;

    private String address;

    private String email;

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
    /**
     * miner数量
     */
    private int amountMiner;

    private LocalDateTime createTime;
}
