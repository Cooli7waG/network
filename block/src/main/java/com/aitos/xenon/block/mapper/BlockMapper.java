package com.aitos.xenon.block.mapper;

import com.aitos.xenon.block.api.domain.dto.BlockSearchDto;
import com.aitos.xenon.block.api.domain.vo.BlockVo;
import com.aitos.xenon.block.domain.Block;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BlockMapper {

    void save(Block block);

    Block getCurrentBlock();

    IPage<BlockVo> list(Page<BlockVo> page,@Param("queryParams") BlockSearchDto queryParams);

    void updateMerkleRoot(@Param("height")Long height,@Param("hash")String hash,@Param("merkleRoot") String merkleRoot);

    List<BlockVo> findListByHeight(@Param("startHeight")Long startHeight, @Param("endHeight")Long endHeight);
}
