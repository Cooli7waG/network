package com.aitos.xenon.block.mapper;

import com.aitos.xenon.block.domain.EpochReward;
import org.apache.ibatis.annotations.Param;

public interface EpochRewardMapper {
    EpochReward findByEpoch(@Param("epoch") Long epoch);
}
