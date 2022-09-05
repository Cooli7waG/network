package com.aitos.xenon.block.api.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PoggReportDataVo {
    private String address;
    private String ownerAddress;
    private Long epoch;
    /**
     * 交易hash
     */
    private String hash;
    private Long height;

    private long power;

    private long total;
    /**
     * 设备采集时间
     */
    private long timestamp;
    /**
     * 服务器入库时间
     */
    private LocalDateTime createTime;
}
