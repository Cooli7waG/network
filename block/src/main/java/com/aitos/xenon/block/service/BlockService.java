package com.aitos.xenon.block.service;

import com.aitos.xenon.block.api.domain.dto.BlockSearchDto;
import com.aitos.xenon.block.api.domain.vo.BlockVo;
import com.aitos.xenon.block.domain.Block;
import com.aitos.xenon.device.api.domain.dto.DeviceSearchDto;
import com.baomidou.mybatisplus.core.metadata.IPage;

public interface BlockService {

    void genBlock();

    Block getCurrentBlock();

    IPage<BlockVo> list(BlockSearchDto queryParams);
}
