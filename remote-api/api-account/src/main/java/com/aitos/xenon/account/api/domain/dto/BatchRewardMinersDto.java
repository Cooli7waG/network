package com.aitos.xenon.account.api.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BatchRewardMinersDto {
    private String address;

    private BigDecimal reward;

    private Long height;
}
