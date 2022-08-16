package com.aitos.xenon.block.service;

import com.aitos.xenon.block.api.domain.PoggReport;
import com.aitos.xenon.block.api.domain.PoggRewardMiner;
import com.aitos.xenon.block.api.domain.dto.PoggReportDto;

import com.aitos.xenon.block.domain.PoggCommit;
import com.baomidou.mybatisplus.core.metadata.IPage;


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

    /**
     * 根据Miner address 获取Report列表
     * @param address
     * @return
     */
    IPage<PoggReport> getReportByMinerAddress(PoggReportDto address);

    /**
     * 根据Miner address 获取Reward列表
     * @param address
     * @return
     */
    IPage<PoggRewardMiner> getRewardByMinerAddress(PoggReportDto address);
}
