package com.aitos.message.push.api;


import com.aitos.message.push.api.domain.dto.PushMessageDto;
import com.aitos.xenon.core.model.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "wake-push")
public interface RemotePushService {
    /**
     * 发送邮件
     *
     * @return
     */
    @PostMapping("/push/mail")
    Result pushMail(@RequestBody PushMessageDto pushMessageDto) ;
}
