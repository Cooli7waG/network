package com.aitos.xenon.device.api.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

@Data
public class DeviceRegisterDto {

    private Integer version;

    private String address;

    private Integer minerType;

    private String maker;

    private String signature;

    private String txData;

    private String txHash;
}
