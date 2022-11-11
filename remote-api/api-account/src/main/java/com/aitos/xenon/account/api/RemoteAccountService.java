package com.aitos.xenon.account.api;


import com.aitos.xenon.account.api.domain.dto.AccountRegisterDto;
import com.aitos.xenon.account.api.domain.vo.AccountVo;
import com.aitos.xenon.account.api.domain.vo.BmtStatisticsVo;
import com.aitos.xenon.core.model.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.math.BigInteger;

@FeignClient(name = "account")
public interface RemoteAccountService {

    @PostMapping("/account/register")
    Result<Long> register(@RequestBody AccountRegisterDto accountRegisterDto);

    @GetMapping("/account/{address}")
    Result<AccountVo> findByAddress( @PathVariable("address") String address);

    @GetMapping("/account/nonce/{address}")
    Result<Long> getNonce(@PathVariable("address") String address);

    @GetMapping("/account/bmtCirculation")
    Result<String> bmtCirculation();

    @GetMapping("/account/bmtStatistics")
    Result<BmtStatisticsVo> bmtStatistics();
}
