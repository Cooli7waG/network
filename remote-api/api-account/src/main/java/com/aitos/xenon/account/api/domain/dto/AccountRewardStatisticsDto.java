package com.aitos.xenon.account.api.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AccountRewardStatisticsDto {

    private String address;

    private String ownerAddress;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
