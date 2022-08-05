package com.aitos.xenon.block.scheduler;

import com.aitos.xenon.block.service.BlockService;
import com.aitos.xenon.block.service.PoggService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class ScheduledService {

    @Autowired
    private BlockService blockService;

    @Autowired
    private PoggService poggService;

    private static int POGG_COMMIT_EPOCH=60;
    private int index=0;
    /**
     * 产生区块和挑战
     */
    @Scheduled(cron = "${schedule.genBlock}")
    @Transactional(rollbackFor = Exception.class)
    public void genBlockTask(){
        log.info("block create");
        blockService.genBlock();
        if(index%POGG_COMMIT_EPOCH==0){
            log.info("poggCommitTask");
            poggService.commit();
        }
        index++;
    }

    /**
     * commit
     * 每小时执行
     *//*
    @Scheduled(cron = "0 0 * * * *")
    @Transactional(rollbackFor = Exception.class)
    public void poggCommitTask(){
        log.info("poggCommitTask");
        poggService.commit();
    }*/
    /**
     *  奖励计算
     */
    @Scheduled(cron = "30 0 * * * *")
    @Transactional(rollbackFor = Exception.class)
    public void rewardCalculationTask(){
        log.info("rewardCalculationTask");
        poggService.rewardCalculation();
    }

    /**
     *  奖励发放
     */
    @Scheduled(cron = "0 30 * * * *")
    @Transactional(rollbackFor = Exception.class)
    public void giveOutRewardsTask(){
        log.info("rewardCalculationTask");
        poggService.giveOutRewards();
    }
}