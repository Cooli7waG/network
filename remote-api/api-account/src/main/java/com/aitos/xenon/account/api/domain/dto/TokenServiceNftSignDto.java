package com.aitos.xenon.account.api.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TokenServiceNftSignDto {

    private String ownerAddress;

    private String minerAddress;

    private Long deadline;
}
