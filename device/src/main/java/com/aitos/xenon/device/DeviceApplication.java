package com.aitos.xenon.device;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients(basePackages ={
        "com.aitos.xenon.account.api",
        "com.aitos.xenon.fundation.api",
        "com.aitos.xenon.block.api",
        "com.aitos.xenon.device.api",
})
@MapperScan("com.aitos.xenon.device.mapper")
@EnableScheduling
public class DeviceApplication {
    public static void main(String[] args)
    {
        SpringApplication.run(DeviceApplication.class, args);
    }
}
