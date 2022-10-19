package com.aitos.xenon.fundation.controller;

import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.core.utils.BeanConvertor;
import com.aitos.xenon.fundation.api.domain.vo.MakerVo;
import com.aitos.xenon.fundation.model.Maker;
import com.aitos.xenon.fundation.service.MakerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/maker")
public class MakerController {

    @Autowired
    private MakerService makerService;


    @GetMapping("/{id}")
    public Result<MakerVo> findById(@PathVariable("id")Long id){
        Maker maker = makerService.findById(id);
        MakerVo makerVo = BeanConvertor.toBean(maker, MakerVo.class);
        return Result.ok(makerVo);
    }

    @GetMapping("/address/{address}")
    public Result<MakerVo> findByAddress(@PathVariable("address")String address){
        Maker maker = makerService.findByAddress(address);
        MakerVo makerVo = BeanConvertor.toBean(maker, MakerVo.class);
        return Result.ok(makerVo);
    }

    @GetMapping("/findByMaker")
    public Result<MakerVo> findByMaker(@RequestParam("maker")String makerName){
        Maker maker = makerService.findByMaker(makerName);
        MakerVo makerVo = BeanConvertor.toBean(maker, MakerVo.class);
        return Result.ok(makerVo);
    }

    @GetMapping("/findAll")
    public Result<List<MakerVo>> findAll(){
        List<Maker> makerList = makerService.findAll();
        List<MakerVo> makerVoList = BeanConvertor.toList(makerList, MakerVo.class);
        return Result.ok(makerVoList);
    }
}
