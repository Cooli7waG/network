package com.aitos.xenon.account.api.domain.dto;

import lombok.Data;

@Data
public class AccountRewardStatisticsDayDto {

    private String address;

    private Integer days=1;
}
