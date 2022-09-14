package com.aitos.xenon.device.api;

import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.device.api.domain.dto.PushMessageDto;
import com.aitos.xenon.device.api.domain.vo.GameMiner;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "gameminer",url = "http://localhost:9509")
public interface RemoteGameMinerService {

    @PostMapping("/game/miner/start")
    Result start(@RequestBody GameMiner virtualMiner);

    @PostMapping("/game/miner/terminate")
    Result terminate(@RequestBody GameMiner virtualMiner);

    @PostMapping("/game/miner/register")
    Result<String> register();

    /**
     * 发送邮件
     *
     * @return
     */
    @PostMapping("/push/mail")
    Result pushMail(@RequestBody PushMessageDto pushMessageDto) ;
}
