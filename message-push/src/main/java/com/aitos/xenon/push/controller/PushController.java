package com.aitos.xenon.push.controller;

import com.aitos.message.push.api.domain.dto.PushMessageDto;

import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.core.utils.BeanConvertor;
import com.aitos.xenon.push.domain.PushMessage;
import com.aitos.xenon.push.service.PushService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/push")
@Slf4j
public class PushController {

    @Autowired
    private PushService pushService;
    /**
     * 发送邮件
     */
    @PostMapping("/mail")
    public Result pushMail(@RequestBody PushMessageDto pushMessageDto) throws Exception {
        log.info("pushMail.params={}", JSON.toJSONString(pushMessageDto));
        PushMessage pushMessage= BeanConvertor.toBean(pushMessageDto,PushMessage.class);
        pushMessage.setMessageType(1);
        pushService.pushMessageToMail(pushMessage);
        return Result.ok();
    }
}
