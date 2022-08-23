package com.aitos.xenon.fundation.api;


import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.fundation.api.domain.dto.AirdropDto;
import com.aitos.xenon.fundation.api.domain.dto.RegisterDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Fundation远程方法调用
 * @author xymoc
 */
@FeignClient(name = "fundation")
public interface RemoteFundationService {

    @PostMapping("/fundation/register")
    public Result<String> register(@RequestBody String registerDto);

    @PostMapping("/fundation/airdrop")
    public Result<String> airdrop(@RequestBody String airdropDto);
}
