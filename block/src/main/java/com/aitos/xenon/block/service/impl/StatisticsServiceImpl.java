package com.aitos.xenon.block.service.impl;

import com.aitos.xenon.account.api.RemoteAccountService;
import com.aitos.xenon.block.domain.BlockChainStatistics;
import com.aitos.xenon.block.domain.MinerStatistics;
import com.aitos.xenon.block.mapper.StatisticsMapper;
import com.aitos.xenon.block.service.BlockService;
import com.aitos.xenon.block.service.StatisticsService;
import com.aitos.xenon.core.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private StatisticsMapper statisticsMapper;

    @Autowired
    private BlockService blockService;

    @Autowired
    private RemoteAccountService remoteAccountService;

    @Override
    public BlockChainStatistics blockchainstats() {
        Result<String> result= remoteAccountService.bmtCirculation();
        BlockChainStatistics  blockChainStatistics=new BlockChainStatistics();
        blockChainStatistics.setVirtualMiners(statisticsMapper.virtualMinersCount());
        blockChainStatistics.setLightSolarMiners(statisticsMapper.lightSolarMinersCount());
        blockChainStatistics.setTransactions(statisticsMapper.transactionsCount());
        blockChainStatistics.setTokenSupply(result.getData());
        blockChainStatistics.setBlocks(blockService.getBlock().getHeight());
        return blockChainStatistics;
    }

    @Override
    public MinerStatistics minerStats() {
        return statisticsMapper.minerStats();
    }
}
