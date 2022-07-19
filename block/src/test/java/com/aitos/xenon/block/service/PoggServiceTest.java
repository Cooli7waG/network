package com.aitos.xenon.block.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PoggServiceTest {

    @Autowired
    private PoggService  poggService;

    @Test
    public void test_reward(){
        poggService.reward();
    }
}
