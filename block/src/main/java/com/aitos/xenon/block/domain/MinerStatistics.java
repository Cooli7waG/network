package com.aitos.xenon.block.domain;

import lombok.Data;

@Data
public class MinerStatistics {
    /**
     * 全网总功率
     */
    private float  totalPowerLow;
    /**
     * 全网总功率（高）
     */
    private float totalPowerHigh;
    /**
     * 全网总发电量
     */
    private float totalChargeVol;
    /**
     * 全网总用电量
     */
    private Long totalUsageVol;
}
