package com.aitos.xenon.block.service.impl;

import com.aitos.xenon.block.mapper.BlockMapper;
import com.aitos.xenon.block.domain.Block;
import com.aitos.xenon.block.service.BlockService;
import com.aitos.xenon.block.service.PoggService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BlockServiceImpl implements BlockService {

    @Autowired
    private BlockMapper blockMapper;



    @Override

    public void genBlock() {
        blockMapper.genBlock();


    }

    @Override
    public Block getBlock() {
        return blockMapper.getBlock();
    }
}
