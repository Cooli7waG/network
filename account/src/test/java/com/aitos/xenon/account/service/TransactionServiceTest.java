package com.aitos.xenon.account.service;

import com.aitos.xenon.account.api.domain.dto.PoggRewardDto;
import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TransactionServiceTest {

    @Autowired
    private TransactionService  transactionService;
    @Test
    public void test_save_sign(){
        //PoggRewardDto poggRewardDto= JSON.parseObject();
        //transactionService.poggReward();
    }
}
