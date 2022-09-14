package com.aitos.xenon.account.api.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AccountRewardStatisticsDto {

    private String address;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
