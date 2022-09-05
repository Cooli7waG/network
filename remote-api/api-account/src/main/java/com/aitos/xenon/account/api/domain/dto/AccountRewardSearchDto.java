package com.aitos.xenon.account.api.domain.dto;

import com.aitos.xenon.core.model.QueryParams;
import lombok.Data;

@Data
public class AccountRewardSearchDto extends QueryParams {
    private String address;
}
