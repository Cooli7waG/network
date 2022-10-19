package com.aitos.xenon.block.service;

import com.aitos.xenon.block.domain.EpochReward;

public interface EpochRewardService {
    EpochReward findByEpoch(Long epoch);
}
