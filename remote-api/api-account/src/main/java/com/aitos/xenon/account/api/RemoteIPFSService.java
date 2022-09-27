package com.aitos.xenon.account.api;


import com.aitos.xenon.account.api.domain.dto.IPFSPutDto;
import com.aitos.xenon.account.api.domain.dto.IPFSSearchDto;
import com.aitos.xenon.account.api.domain.dto.RemoteKMSSignDto;
import com.aitos.xenon.core.model.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ipfs",url = "${remoteService.ipfsUrl}")
public interface RemoteIPFSService {

    @PostMapping("/putRECData/v1")
    Result putRECData(@RequestBody IPFSPutDto ipfsPutDto);

    @PostMapping("/getRECCid/v1")
    Result putRECData(@RequestBody IPFSSearchDto ipfsSearchDto);
}
