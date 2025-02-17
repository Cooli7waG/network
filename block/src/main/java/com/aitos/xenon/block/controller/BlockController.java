package com.aitos.xenon.block.controller;

import com.aitos.xenon.block.api.domain.dto.BlockSearchDto;
import com.aitos.xenon.block.api.domain.vo.BlockVo;
import com.aitos.xenon.block.domain.Block;
import com.aitos.xenon.block.service.BlockService;
import com.aitos.xenon.core.model.Page;
import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.core.utils.BeanConvertor;
import com.aitos.xenon.device.api.domain.dto.DeviceSearchDto;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/block")
public class BlockController {

    @Autowired
    private BlockService blockService;

    @GetMapping("/height")
    public Result<Long> getBlockHeight(){
        Block block=blockService.getCurrentBlock();
        return Result.ok(block.getHeight());
    }

    @GetMapping("/query")
    public Result<BlockVo> query(String height){
        Block block=blockService.getCurrentBlock();
        BlockVo blockVo= BeanConvertor.toBean(block,BlockVo.class);
        return Result.ok(blockVo);
    }

    @GetMapping("/list")
    public Result<Page<BlockVo>> list(BlockSearchDto queryParams){
        IPage<BlockVo> listPage= blockService.list(queryParams);

        Page<BlockVo> deviceVoPage=new Page<BlockVo>(listPage.getTotal(),listPage.getRecords());
        return Result.ok(deviceVoPage);
    }
}
