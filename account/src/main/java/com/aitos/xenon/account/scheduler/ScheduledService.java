package com.aitos.xenon.account.scheduler;

import com.aitos.xenon.account.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class ScheduledService {

    @Autowired
    private TransactionService transactionService;
    /**
     * 上报report 交易
     */
    @Scheduled(cron = "${schedule.reportDataToIpfs}")
    @Transactional(rollbackFor = Exception.class)
    public void reportDataToIpfsTask(){
        log.info("reportDataToIpfsTask");
        transactionService.reportDataToIpfs();
    }

}