package com.aitos.xenon.account.api.domain.dto;

import com.aitos.xenon.core.model.QueryParams;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class TransactionSearchDto extends QueryParams {
    private Long height;

    private String address;

    private String hash;

    private Integer txType;
}
