package com.aitos.xenon.push.service.impl;



import com.aitos.xenon.push.common.Constant;
import com.aitos.xenon.push.domain.PushMessage;
import com.aitos.xenon.push.domain.PushMessageLog;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;


/**
 * Created by ytx on 2015/12/14.
 */
@Service
@Slf4j
public class MailService {
    @Autowired
    private JavaMailSenderImpl mailSender;

    @Async
    public void push(PushMessage pushMessage) throws Exception{
        PushMessageLog pushMessageLog= new PushMessageLog();
        try{

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(mailSender.getUsername());
            messageHelper.setTo(pushMessage.getTo());
            messageHelper.setSubject(pushMessage.getTitile());
            messageHelper.setText(pushMessage.getContent(),true);
            mailSender.send(mimeMessage);

            pushMessageLog.setStatus(Constant.Status.SUCCESS);
            String json= JSON.toJSONString(pushMessage);
            log.info("mail.success:{}", json);
        } catch (Exception e){
            log.error("mail.error={}",pushMessage.getTo(),e);
            pushMessageLog.setErrorMsg(e.getMessage());
            pushMessageLog.setStatus(Constant.Status.FAIL);
        } finally {

        }
    }
}
