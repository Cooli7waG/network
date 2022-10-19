package com.aitos.xenon.device.api.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class OwnerBindApplyDto {
    @NotEmpty(message = "imei can not be empty")
    private String imei;

    @NotNull(message = " makerId can not be empty")
    private Long makerId;

    @NotEmpty(message = "ownerSignature can not be empty")
    private String ownerSignature;
}
