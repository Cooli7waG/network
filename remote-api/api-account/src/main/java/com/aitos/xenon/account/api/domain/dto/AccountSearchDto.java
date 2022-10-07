package com.aitos.xenon.account.api.domain.dto;

import com.aitos.xenon.core.model.QueryParams;
import lombok.Data;

@Data
public class AccountSearchDto extends QueryParams {

    private String address;

    private Integer accountType;
}
