package com.aitos.xenon.account.api.domain.dto;

import lombok.Data;

@Data
public class RemoteKMSSignDto {

    private String keyId;

    private String hash;

    private String rawData;
}
