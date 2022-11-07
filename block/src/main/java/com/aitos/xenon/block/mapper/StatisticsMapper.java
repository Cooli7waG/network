package com.aitos.xenon.block.mapper;

import com.aitos.xenon.block.domain.MinerStatistics;

public interface StatisticsMapper {

    long virtualMinersCount();
    long lightSolarMinersCount();
    long poggChallengesCount();
    long transactionsCount();
    double tokenSupplyCount();
    MinerStatistics minerStats();

    double sumTokenSupply();
}
