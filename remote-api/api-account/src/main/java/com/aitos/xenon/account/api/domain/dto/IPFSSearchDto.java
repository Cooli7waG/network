package com.aitos.xenon.account.api.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class IPFSSearchDto {

    private String startDate;
    private String endDate;

    private String ownerAddress;
}
