package com.aitos.xenon.block.service.impl;

import com.aitos.xenon.block.api.domain.dto.BlockSearchDto;
import com.aitos.xenon.block.api.domain.vo.BlockVo;
import com.aitos.xenon.block.domain.Block;
import com.aitos.xenon.block.mapper.BlockMapper;
import com.aitos.xenon.block.service.BlockService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class BlockServiceImpl implements BlockService {

    @Autowired
    private BlockMapper blockMapper;



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void genBlock() {
        Block block= getCurrentBlock();
        Block newBlock=new Block();
        newBlock.setHeight(block.getHeight()+1);
        newBlock.setCreateTime(LocalDateTime.now());
        Duration duration = Duration.between(block.getCreateTime(), newBlock.getCreateTime());
        newBlock.setBlockIntervalTime(duration.toSeconds());
        blockMapper.save(newBlock);
    }

    @Override
    public Block getCurrentBlock() {
        return blockMapper.getCurrentBlock();
    }

    @Override
    public IPage<BlockVo> list(BlockSearchDto queryParams) {
        Page<BlockVo> page=new Page<BlockVo>(queryParams.getOffset(),queryParams.getLimit());
        IPage<BlockVo> pageResult=blockMapper.list(page,queryParams);
        return pageResult;
    }


}
