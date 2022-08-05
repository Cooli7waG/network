package com.aitos.xenon.device.api.domain.dto;
import lombok.Data;

import java.math.BigInteger;

@Data
public class ClaimDto {
    private int version;

    private String minerAddress;

    private String ownerAddress;
    /**
     * 基金会签名
     */
    private String signature;
}
