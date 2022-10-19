package com.aitos.xenon.block.domain;

import lombok.Data;

@Data
public class MinerStatistics {

    private Integer miners;
    /**
     * 全网总功率
     */
    private float  totalPowerLow;
    /**
     * 全网总发电量
     */
    private float totalChargeVol;

}
