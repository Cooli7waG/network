package com.aitos.xenon.device.api.domain.dto;

import lombok.Data;

@Data
public class DeviceRegisterDto {

    private Integer version;

    private String address;

    private Integer minerType;

    private String maker;

    private String foundationSignature;

    private String txData;

    private String txHash;
}
