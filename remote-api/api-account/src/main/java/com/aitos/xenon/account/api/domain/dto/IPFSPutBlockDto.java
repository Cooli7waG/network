package com.aitos.xenon.account.api.domain.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class IPFSPutBlockDto {

    private String blockRange;

    private String dateTime;

    private List<IPFSPutBlockDataDto> data;
}
