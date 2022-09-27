package com.aitos.xenon.account.api;


import com.aitos.xenon.account.api.domain.dto.RemoteKMSSignDto;
import com.aitos.xenon.account.api.domain.dto.TokenServiceWithdrawDto;
import com.aitos.xenon.account.api.domain.vo.RemoteKMSSignVo;
import com.aitos.xenon.core.model.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "KMS",url = "${remoteService.kmsUrl}")
public interface RemoteKMSService {

    @PostMapping("/sign/v1")
    Result<RemoteKMSSignVo> sign(@RequestBody RemoteKMSSignDto remoteKMSSignDto);
}
