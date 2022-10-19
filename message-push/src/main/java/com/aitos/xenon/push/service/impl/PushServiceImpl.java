package com.aitos.xenon.push.service.impl;


import com.aitos.xenon.push.common.Constant;
import com.aitos.xenon.push.domain.PushMessage;
import com.aitos.xenon.push.domain.PushTemplate;
import com.aitos.xenon.push.service.PushService;
import com.aitos.xenon.push.service.PushTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PushServiceImpl implements PushService {

    @Autowired
    private MailService mailService;

    @Autowired
    private PushTemplateService pushTemplateService;


    @Override
    public void pushMessageToMail(PushMessage pushMessage) throws Exception{
        PushTemplate pushTemplate=pushTemplateService.findById(pushMessage.getTemplateId());

        StringSubstitutor sub = new StringSubstitutor(pushMessage.getCustomMap());
        String content=sub.replace(pushTemplate.getContent());
        pushMessage.setTitile(pushTemplate.getName());
        pushMessage.setContent(content);
        mailService.push(pushMessage);
    }

    @Override
    public void pushMessage(PushMessage pushMessage) throws Exception {
        if(pushMessage.getMessageType().equals(Constant.MessageType.EMAIL)){
            mailService.push(pushMessage);
        }
    }
}