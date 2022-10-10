package com.aitos.xenon.account.api.domain.dto;

import com.aitos.xenon.account.api.domain.vo.TransactionToIpfsVo;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class IPFSPutDto {

    private String date;

    private String ownerAddress;

    private String minerAddress;

    private List<TransactionToIpfsVo> data;
}
