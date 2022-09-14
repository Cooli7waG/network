package com.aitos.xenon.arkreen3rdpartymaker.api;


import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "account")
public interface RemoteAccountService {


}
