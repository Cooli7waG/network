package com.aitos.xenon.block.api.domain;

import lombok.Data;

/**
 * PoggReport
 * @author xymoc
 */
@Data
public class PoggReport {

    private Long id;
    private String address;
    private Long epoch;
    /**
     * 功率
     */
    private Long power;
    /**
     * '发电量'
     */
    private Long total;
    private Long timestamp;
}
