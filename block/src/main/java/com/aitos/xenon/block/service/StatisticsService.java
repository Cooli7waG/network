package com.aitos.xenon.block.service;

import com.aitos.xenon.block.domain.BlockChainStatistics;
import com.aitos.xenon.block.domain.MinerStatistics;

public interface StatisticsService {

    BlockChainStatistics blockchainstats();

    MinerStatistics minerStats();
}
