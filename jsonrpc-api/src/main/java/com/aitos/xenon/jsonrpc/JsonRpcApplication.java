package com.aitos.xenon.jsonrpc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@EnableFeignClients(basePackages ={
        "com.aitos.xenon.account.api",
        "com.aitos.xenon.device.api",
        "com.aitos.xenon.block.api"
})
@EnableDiscoveryClient
public class JsonRpcApplication {

    public static void main(String[] args)
    {
        SpringApplication.run(JsonRpcApplication.class, args);
    }
}
