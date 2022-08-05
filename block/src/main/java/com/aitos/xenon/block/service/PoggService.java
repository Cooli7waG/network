package com.aitos.xenon.block.service;

import com.aitos.xenon.block.domain.PoggCommit;
import com.aitos.xenon.block.domain.PoggChallengeRecord;

import java.util.List;

public interface PoggService {
    void commit();

    PoggCommit  findCurrentCommit();
    /**
     * 奖励计算
     */
     void rewardCalculation();

    /**
     * 发放奖励
     */
    void giveOutRewards();
}
