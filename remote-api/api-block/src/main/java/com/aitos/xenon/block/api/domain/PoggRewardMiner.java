package com.aitos.xenon.block.api.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
public class PoggRewardMiner {


    private String address;

    /**
     *  owner address
     */
    private String ownerAddress;

    /**
     * 奖励数量
     */
    private BigDecimal amount;

    private Date createTime;
}
