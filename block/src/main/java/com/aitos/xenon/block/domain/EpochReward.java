package com.aitos.xenon.block.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class EpochReward {
    private Long startEpoch;

    private Long endEpoch;

    private BigDecimal tokenNumber;
}
