package com.aitos.xenon.block.controller;

import com.aitos.xenon.account.api.RemoteIPFSService;
import com.aitos.xenon.account.api.domain.dto.IPFSSearchBlockCidDto;
import com.aitos.xenon.account.api.domain.dto.IPFSSearchDto;
import com.aitos.xenon.block.service.PoggReportService;
import com.aitos.xenon.core.model.Result;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;

@RestController
@RequestMapping("/ipfs/")
@Slf4j
public class IPFSDataController {
    @Autowired
    private RemoteIPFSService remoteIPFSService;

    @Autowired
    private PoggReportService poggReportService;

    @PostMapping("/reportData")
    public Result<HashMap<String,Object>> reportData(@RequestBody IPFSSearchDto ipfsSearchDto){
        LocalDateTime startTime=ipfsSearchDto.getStartDate().atTime(0,0,0);
        LocalDateTime endTime=ipfsSearchDto.getEndDate().atTime(23,59,59);
        long totalEnergyGeneration = poggReportService.findTotalEnergyGeneration(startTime, endTime, ipfsSearchDto.getOwnerAddress());
        Result<String> recCidResult = remoteIPFSService.getRECCid(ipfsSearchDto);
        log.info("recCidResult={}", JSON.toJSONString(recCidResult));
        HashMap<String,Object> result=new HashMap<>();
        result.put("totalEnergyGeneration",totalEnergyGeneration);
        result.put("cid",recCidResult.getData());
        return Result.ok(result);
    }

    @PostMapping("/block")
    public Result<String> block(@RequestBody IPFSSearchBlockCidDto iPFSSearchBlockCidDto){
        Result<String> blockCid = remoteIPFSService.getBlockCid(iPFSSearchBlockCidDto);
        return blockCid;
    }
}
