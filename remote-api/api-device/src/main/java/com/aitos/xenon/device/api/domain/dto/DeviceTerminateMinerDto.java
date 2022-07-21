package com.aitos.xenon.device.api.domain.dto;

import lombok.Data;

@Data
public class DeviceTerminateMinerDto {

    private Integer version;

    private String address;

    private String nonce;

    private String foundationSignature;
}
