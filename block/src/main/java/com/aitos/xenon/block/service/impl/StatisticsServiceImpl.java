package com.aitos.xenon.block.service.impl;

import com.aitos.xenon.account.api.RemoteAccountService;
import com.aitos.xenon.account.api.domain.vo.BmtStatisticsVo;
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

    private final String uSDBmtMarketPrice="1";

    private final String totalBMTMarket = "10000000000";

    @Override
    public BlockChainStatistics blockchainstats() {
        BlockChainStatistics blockChainStatistics=new BlockChainStatistics();
        blockChainStatistics.setUSDBmtMarketPrice(uSDBmtMarketPrice);
        blockChainStatistics.setTotalBMTMarket(totalBMTMarket);
        Double tokenSupply =  statisticsMapper.sumTokenSupply();
        blockChainStatistics.setTokenSupply(tokenSupply.toString());
        return blockChainStatistics;
    }

    @Override
    public MinerStatistics minerStats() {
        return statisticsMapper.minerStats();
    }
}
