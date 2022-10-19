package com.aitos.xenon.account.api.domain.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class IPFSSearchDto {

    private LocalDate startDate;
    private LocalDate endDate;

    private String ownerAddress;
}
