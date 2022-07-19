package com.aitos.xenon.block.controller;

import com.aitos.xenon.block.api.domain.vo.BlockVo;
import com.aitos.xenon.block.domain.Block;
import com.aitos.xenon.block.service.BlockService;
import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.core.utils.BeanConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/block")
public class BlockController {

    @Autowired
    private BlockService blockService;

    @GetMapping("/height")
    public Result<Long> getBlockHeight(){
        Block block=blockService.getBlock();
        return Result.ok(block.getHeight());
    }

    @GetMapping("/query")
    public Result<BlockVo> query(String height){
        Block block=blockService.getBlock();
        BlockVo blockVo= BeanConvertor.toBean(block,BlockVo.class);
        return Result.ok(blockVo);
    }
}
