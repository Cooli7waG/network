package com.aitos.xenon.push.service;


import com.aitos.xenon.push.domain.PushMessage;

public interface PushService {

    void pushMessageToMail(PushMessage pushMessage) throws Exception;

    void pushMessage(PushMessage pushMessage) throws Exception;
}