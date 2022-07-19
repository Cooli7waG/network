package com.aitos.xenon.jsonrpc.common;

import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Component;

@Component
public class SpringContextHolder extends ApplicationObjectSupport {
 
    public Task getTask(String beanName){
        return super.getApplicationContext().getBean(beanName , Task.class);
    }
}