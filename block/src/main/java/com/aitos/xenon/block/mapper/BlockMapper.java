package com.aitos.xenon.block.mapper;

import com.aitos.xenon.block.api.domain.dto.BlockSearchDto;
import com.aitos.xenon.block.api.domain.vo.BlockVo;
import com.aitos.xenon.block.domain.Block;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BlockMapper {

    void save(Block block);

    Block getCurrentBlock();

    IPage<BlockVo> list(Page<BlockVo> page,@Param("queryParams") BlockSearchDto queryParams);
}
