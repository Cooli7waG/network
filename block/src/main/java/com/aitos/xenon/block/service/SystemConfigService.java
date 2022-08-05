package com.aitos.xenon.block.service;

import com.aitos.xenon.block.domain.SystemConfig;

public interface SystemConfigService {

    void update(SystemConfig systemConfig);

    SystemConfig findConfig(boolean refresh);
}
