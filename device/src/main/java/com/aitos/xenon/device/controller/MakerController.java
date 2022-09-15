package com.aitos.xenon.device.controller;

import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.fundation.api.RemoteMakerService;
import com.aitos.xenon.fundation.api.domain.vo.MakerVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/maker")
public class MakerController {
    @Autowired
    private RemoteMakerService remoteMakerService;

    @GetMapping("/findAll")
    public Result<List<MakerVo>> findAll(){
        Result<List<MakerVo>> makerVoResult = remoteMakerService.findAll();
        return makerVoResult;
    }
}
