package com.aitos.xenon.account;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients(basePackages ={
        "com.aitos.xenon.block.api",
        "com.aitos.xenon.device.api",
        "com.aitos.xenon.account.api"
})
@MapperScan("com.aitos.xenon.account.mapper")
@EnableScheduling
public class AccountApplication {

    public static void main(String[] args)
    {
        SpringApplication.run(AccountApplication.class, args);
    }
}
