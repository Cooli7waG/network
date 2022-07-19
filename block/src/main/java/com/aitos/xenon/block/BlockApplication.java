package com.aitos.xenon.block;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableFeignClients(basePackages ={
        "com.aitos.xenon.account.api",
        "com.aitos.xenon.device.api"
})
@MapperScan("com.aitos.xenon.block.mapper")
@EnableScheduling
@EnableTransactionManagement
public class BlockApplication {
    public static void main(String[] args)
    {
        SpringApplication.run(BlockApplication.class, args);
    }
}
