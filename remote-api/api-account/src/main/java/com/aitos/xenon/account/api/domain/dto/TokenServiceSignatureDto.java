package com.aitos.xenon.account.api.domain.dto;

import lombok.Data;

@Data
public class TokenServiceSignatureDto {
    /**
     * 提现用户
     */
    private String recipient;
    /**
     * 提现数额
     */
    private String value;
    /**
     * 提现用户nonce
     */
    private Long nonce;
}
