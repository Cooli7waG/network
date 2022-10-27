package com.aitos.xenon.account.api.domain.dto;

import com.aitos.xenon.core.model.BaseModel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountRegisterDto extends BaseModel {

    private String address;

    private String email;

    private BigDecimal balance;

    private Long nonce;

    /**
     * 1.owner账户；2miner账户
     */
    private Integer  accountType;
}
