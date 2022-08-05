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

    @Autowired
    private RemoteAccountService remoteAccountService;

    private final String uSDBmtMarketPrice="1";

    @Override
    public BlockChainStatistics blockchainstats() {
        Result<BmtStatisticsVo> result= remoteAccountService.bmtStatistics();
        BmtStatisticsVo bmtStatisticsVo=result.getData();
        BlockChainStatistics  blockChainStatistics=new BlockChainStatistics();
        blockChainStatistics.setUSDBmtMarketPrice(uSDBmtMarketPrice);
        blockChainStatistics.setTotalBMTMarket(bmtStatisticsVo.getTotalBMTMarket());
        blockChainStatistics.setTokenSupply(bmtStatisticsVo.getTokenSupply());
        return blockChainStatistics;
    }

    @Override
    public MinerStatistics minerStats() {
        return statisticsMapper.minerStats();
    }
}
