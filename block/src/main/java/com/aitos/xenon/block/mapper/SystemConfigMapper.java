package com.aitos.xenon.block.mapper;

import com.aitos.xenon.block.domain.SystemConfig;

public interface SystemConfigMapper {

    void update(SystemConfig systemConfig);

    SystemConfig findConfig();
}
