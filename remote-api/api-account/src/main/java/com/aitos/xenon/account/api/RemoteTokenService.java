package com.aitos.xenon.account.api;


import com.aitos.common.crypto.ecdsa.Hash;
import com.aitos.xenon.account.api.domain.dto.TokenServiceSearchNonceDto;
import com.aitos.xenon.account.api.domain.dto.TokenServiceSignatureDto;
import com.aitos.xenon.account.api.domain.dto.TokenServiceWithdrawDto;
import com.aitos.xenon.account.api.domain.vo.AccountVo;
import com.aitos.xenon.account.api.domain.vo.BmtStatisticsVo;
import com.aitos.xenon.account.api.domain.vo.TokenServiceSearchNonceVo;
import com.aitos.xenon.core.model.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;

@FeignClient(name = "token",url = "${remoteService.tokenUrl}")
public interface RemoteTokenService {

    @PostMapping("/GetNonce")
    Result<TokenServiceSearchNonceVo> fetchNonce(@RequestBody TokenServiceSearchNonceDto tokenServiceSearchNonceDto);

    @PostMapping("/GetSignature")
    Result<HashMap> withdraw(@RequestBody TokenServiceSignatureDto tokenServiceSignatureDto);

}
