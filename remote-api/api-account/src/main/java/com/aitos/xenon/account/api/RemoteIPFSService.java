package com.aitos.xenon.account.api;


import com.aitos.xenon.account.api.domain.dto.*;
import com.aitos.xenon.core.model.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ipfs",url = "${remoteService.ipfsUrl}")
public interface RemoteIPFSService {

    @PostMapping("/putRECData/v1")
    Result putRECData(@RequestBody IPFSPutDto ipfsPutDto);

    @PostMapping("/getRECCid/v1")
    Result getRECCid(@RequestBody IPFSSearchDto ipfsSearchDto);

    @PostMapping("/putBlockData/v1")
    Result putBlockData(@RequestBody IPFSPutBlockDto iPFSPutBlockDto);

    @PostMapping("/getBlockCid/v1")
    Result<String> getBlockCid(@RequestBody IPFSSearchBlockCidDto iPFSSearchBlockCidDto);
}
