package com.aitos.xenon.account.domain;

import lombok.Data;

import java.util.Date;

@Data
public class PoggReportMiner {

    private Long id;
    /**
     * miner address
     */
    private String address;
    /**
     * power
     */
    private Long power;
    /**
     * total
     */
    private Long total;
    /**
     * txHash
     */
    private String hash;
    /**
     * blockHeight
     */
    private Long height;
    /**
     * 采样时间
     */
    private Long timestamp;
    private Date createTime;

}
