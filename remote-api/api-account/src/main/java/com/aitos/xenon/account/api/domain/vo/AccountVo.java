package com.aitos.xenon.account.api.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
public class AccountVo {

    private Long id;

    private String address;

    private String web3Address;

    private String balance;

    private Long nonce;

    /**
     * 1.owner账户；2miner账户
     */
    private Integer  accountType;

    private LocalDateTime createTime;
}
