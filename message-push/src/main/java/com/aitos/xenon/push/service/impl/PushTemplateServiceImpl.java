package com.aitos.xenon.push.service.impl;


import com.aitos.xenon.push.domain.PushTemplate;
import com.aitos.xenon.push.mapper.PushTemplateMapper;
import com.aitos.xenon.push.service.PushTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PushTemplateServiceImpl implements PushTemplateService {

    @Autowired
    private PushTemplateMapper pushTemplateMapper;

    @Override
    public PushTemplate findById(Long id) {
        return pushTemplateMapper.findById(id);
    }
}
