package com.aitos.xenon.block.service.impl;

import com.aitos.xenon.block.domain.EpochReward;
import com.aitos.xenon.block.mapper.EpochRewardMapper;
import com.aitos.xenon.block.service.EpochRewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EpochRewardServiceImpl implements EpochRewardService {

    @Autowired
    private EpochRewardMapper epochRewardMapper;

    @Override
    public EpochReward findByEpoch(Long epoch) {
        return epochRewardMapper.findByEpoch(epoch);
    }
}
