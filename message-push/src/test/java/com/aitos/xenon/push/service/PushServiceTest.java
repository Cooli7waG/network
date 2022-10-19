package com.aitos.xenon.push.service;


import com.aitos.xenon.push.PushApplication;
import com.aitos.xenon.push.domain.PushMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(classes = PushApplication.class)
public class PushServiceTest {

    @Autowired
    private PushService  pushService;

    @Test
    public void test_findById() throws Exception {
        PushMessage pushMessage=new PushMessage();
        pushMessage.setMessageType(1);
        pushMessage.setTemplateId(1l);
        pushMessage.setFrom("hnngm@qq.com");
        pushMessage.setTo("hnngm@qq.com");
        pushMessage.setTitile("test");
        pushMessage.setContent("content");

        Map<String,Object> customMap=new HashMap<>();
        customMap.put("url","www.baidu.com");
        pushMessage.setCustomMap(customMap);
        pushService.pushMessageToMail(pushMessage);
    }

}
