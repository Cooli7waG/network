package com.aitos.xenon.block.controller;

import com.aitos.xenon.block.domain.BlockChainStatistics;
import com.aitos.xenon.block.domain.MinerStatistics;
import com.aitos.xenon.block.service.StatisticsService;
import com.aitos.xenon.core.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/blockchainstats")
    public Result<BlockChainStatistics> blockchainstats(){
        BlockChainStatistics blockChainStatistics=statisticsService.blockchainstats();
        return Result.ok(blockChainStatistics);
    }

    @GetMapping("/minerstats")
    public Result<MinerStatistics> minerStats(){
        MinerStatistics minerStatistics=statisticsService.minerStats();
        if(minerStatistics==null){
            minerStatistics=new MinerStatistics();
        }
        return Result.ok(minerStatistics);
    }
}
