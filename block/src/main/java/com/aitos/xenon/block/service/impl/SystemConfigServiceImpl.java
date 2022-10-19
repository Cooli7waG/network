package com.aitos.xenon.block.service.impl;

import com.aitos.xenon.block.domain.SystemConfig;
import com.aitos.xenon.block.mapper.SystemConfigMapper;
import com.aitos.xenon.block.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hnngm
 */
@Service
public class SystemConfigServiceImpl implements SystemConfigService {

    @Autowired
    private SystemConfigMapper systemConfigMapper;

    private static volatile SystemConfig systemConfig=null;

    @Override
    public void update(SystemConfig systemConfig) {
        systemConfigMapper.update(systemConfig);
    }

    @Override
    public SystemConfig findConfig(boolean refresh) {
        if(refresh){
            synchronized (SystemConfig.class){
                    systemConfig=systemConfigMapper.findConfig();
            }
        }else{
            if(null==systemConfig){
                synchronized (SystemConfig.class){
                    if(null==systemConfig){
                        systemConfig=systemConfigMapper.findConfig();
                    }
                }
            }
        }
        return systemConfig;
    }
}
