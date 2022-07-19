package com.aitos.xenon.account;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages ={"com.aitos.xenon.block.api"})
@MapperScan("com.aitos.xenon.account.mapper")
public class AccountApplication {

    public static void main(String[] args)
    {
        SpringApplication.run(AccountApplication.class, args);
    }
}
