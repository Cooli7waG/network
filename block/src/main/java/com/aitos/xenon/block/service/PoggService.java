package com.aitos.xenon.block.service;

import com.aitos.xenon.block.domain.PoggCommit;

public interface PoggService {
    void commit();

    PoggCommit  findCurrentCommit();
    PoggCommit  findByEpoch(Long epoch);
    /**
     * 奖励计算
     */
     void rewardCalculation();

    /**
     * 发放奖励
     */
    void giveOutRewards();

    void blockDataToIpfs();
}
