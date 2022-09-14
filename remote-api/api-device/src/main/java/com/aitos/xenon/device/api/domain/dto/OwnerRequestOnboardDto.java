package com.aitos.xenon.device.api.domain.dto;

import lombok.Data;

@Data
public class OwnerRequestOnboardDto {
    private String imei;
    private String ownerAddress;
    private String minerAddress;
}
