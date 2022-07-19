package com.aitos.xenon.block.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
public class BlockChainStatistics {
    /**
     * Virtual Miner数量
     */
    private long  virtualMiners;
    /**
     * Light Solar Miner数量
     */
    private long lightSolarMiners;
    /**
     * 区块数量（块高）
     */
    private long blocks;
    /**
     * PoGG挑战数量
     */
    private long poggChallenges;

    private long transactions;

    private String tokenSupply;
}
