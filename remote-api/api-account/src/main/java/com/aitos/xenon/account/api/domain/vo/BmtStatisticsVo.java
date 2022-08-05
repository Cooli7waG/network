package com.aitos.xenon.account.api.domain.vo;

import lombok.Data;

@Data
public class BmtStatisticsVo {

    /**
     * BMT市场总量
     */
    private String totalBMTMarket;
    /**
     * BMT流通供应量
     */
    private String tokenSupply;
}
