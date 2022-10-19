package com.aitos.xenon.block.domain;

import lombok.Data;


@Data
public class BlockChainStatistics {
    /**
     * 美元计BMT市场价格
     */
    private String  uSDBmtMarketPrice;

    /**
     * BMT市场总量
     */
    private String totalBMTMarket;

    /**
     * BMT流通供应量
     */
    private String tokenSupply;
}
