package com.aitos.xenon.account.service;

import com.aitos.xenon.account.api.domain.dto.PoggRewardDto;
import com.aitos.xenon.account.domain.Transaction;
import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TransactionServiceTest {

    @Autowired
    private TransactionService  transactionService;

    @Test
    public void test_save_sign(){
        //PoggRewardDto poggRewardDto= JSON.parseObject();
        //transactionService.poggReward();
    }

    @Test
    public void test_get_tx(){
        List<Transaction> all = transactionService.getAll();
        for (Transaction transaction : all) {
            transactionService.abstractTransaction(transaction);
        }
    }
}
