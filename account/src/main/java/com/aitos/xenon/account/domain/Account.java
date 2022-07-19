package com.aitos.xenon.account.domain;

import com.aitos.xenon.core.model.BaseModel;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
public class Account extends BaseModel {

    private String address;

    private String web3Address;

    private String balance;

    private Long nonce;

    /**
     * 1.owner账户；2miner账户
     */
    private Integer  accountType;
}
