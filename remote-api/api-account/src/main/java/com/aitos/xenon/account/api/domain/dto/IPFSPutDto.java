package com.aitos.xenon.account.api.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class IPFSPutDto {

    private String date;

    private String ownerAddress;

    private String minerAddress;

    private List<Object> data;
}
