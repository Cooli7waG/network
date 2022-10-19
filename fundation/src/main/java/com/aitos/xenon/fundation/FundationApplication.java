package com.aitos.xenon.fundation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages ={"com.aitos.xenon.device.api"})
@MapperScan("com.aitos.xenon.fundation.mapper")
public class FundationApplication {

    public static void main(String[] args)
    {
        SpringApplication.run(FundationApplication.class, args);
    }
}
