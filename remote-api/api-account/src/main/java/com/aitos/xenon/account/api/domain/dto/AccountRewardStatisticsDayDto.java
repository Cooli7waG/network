package com.aitos.xenon.account.api.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class AccountRewardStatisticsDayDto {

    @NotEmpty(message = "address can not empty")
    private String address;

    @NotNull(message = "startTime can not empty")
    private LocalDateTime startTime;
    @NotNull(message = "endTime can not empty")
    private LocalDateTime endTime;
}
