package com.aitos.xenon.account.api.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class IPFSPutBlockDataDto {
    private String hash;
    private  Long height;

    private Long blockIntervalTime;

    private String parentHash;

    private String merkleRoot;

    private LocalDateTime createTime;
}
