package com.aitos.xenon.block.domain;

import lombok.Data;

/**
 * 统计miner 的上报的数据数量
 */
@Data
public class PoggReportSubtotalStatistics {

    private String address;

    private String ownerAddress;

    /**
     * miner 类型
     */
    private Integer minerType;

    private int   total;
}
