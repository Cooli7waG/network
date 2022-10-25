package com.aitos.xenon.block.domain;

import lombok.Data;

/**
 * miner 功率数据
 */
@Data
public class PoggReportPowerData {

    private String address;

    private Long epoch;
   
    private Long power;

    private Long timestamp;
}
