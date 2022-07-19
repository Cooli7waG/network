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

    /**
     * 产生区块和挑战
     */
    @Scheduled(cron = "${schedule.genBlock}")
    @Transactional(rollbackFor = Exception.class)
    public void genBlockTask(){
        log.info("block create");
        blockService.genBlock();
        poggService.genChallenge();
    }

    /**
     * 产生挑战
     */
    /*@Scheduled(cron = "${schedule.genChallenge}")
    @Transactional(rollbackFor = Exception.class)
    public void genChallengeTask(){
        log.info("genChallenge create");
        poggService.genChallenge();
    }*/

    /**
     *  发放奖励
     */
    @Scheduled(cron = "${schedule.reward}")
    @Transactional(rollbackFor = Exception.class)
    public void rewardTask(){
        log.info("reward");
        poggService.reward();
    }
}