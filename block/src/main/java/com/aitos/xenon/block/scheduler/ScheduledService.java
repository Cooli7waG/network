package com.aitos.xenon.block.scheduler;

import com.aitos.xenon.block.service.BlockService;
import com.aitos.xenon.block.service.PoggService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
        index=LocalDateTime.now().getMinute()+1;
        if(index%POGG_COMMIT_EPOCH==0){
            log.info("poggCommitTask");
            poggService.commit();
        }
    }
    /**
     *  奖励计算
     */
    @Scheduled(cron = "${schedule.rewardCalculation}")
    @Transactional(rollbackFor = Exception.class)
    public void rewardCalculationTask(){
        log.info("rewardCalculationTask");
        poggService.rewardCalculation();
    }

    /**
     *  奖励发放
     */
    @Scheduled(cron = "${schedule.giveOutRewards}")
    @Transactional(rollbackFor = Exception.class)
    public void giveOutRewardsTask(){
        log.info("giveOutRewardsTask");
        poggService.giveOutRewards();
    }


    /**
     *  上报block交易
     */
    @Scheduled(cron = "${schedule.blockDataToIpfs}")
    public void blockDataToIpfsTask(){
        log.info("blockDataToIpfsTask");
        poggService.blockDataToIpfs();
    }
}