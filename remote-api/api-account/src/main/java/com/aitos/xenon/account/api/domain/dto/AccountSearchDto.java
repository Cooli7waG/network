package com.aitos.xenon.account.api.domain.dto;

import com.aitos.xenon.core.model.BaseModel;
import com.aitos.xenon.core.model.QueryParams;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountSearchDto extends QueryParams {

    private String address;
}
