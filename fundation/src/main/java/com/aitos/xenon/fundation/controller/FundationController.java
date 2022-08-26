package com.aitos.xenon.fundation.controller;

import com.aitos.common.crypto.coder.DataCoder;
import com.aitos.common.crypto.ecdsa.Ecdsa;
import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.fundation.api.domain.dto.AirdropDto;
import com.aitos.xenon.fundation.api.domain.dto.RegisterDto;
import com.aitos.xenon.fundation.service.FundationService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequestMapping("/fundation")
public class FundationController {

    @Value("${foundation.privateKey}")
    private String foundationPrivateKey;

    @PostMapping("/register")
    public Result<String> register(@RequestBody String registerDto){
        log.info("register:{}", registerDto);
        String sign = Ecdsa.sign(foundationPrivateKey, registerDto, DataCoder.BASE58);
        return Result.ok(sign);
    }

    @PostMapping("/airdrop")
    public Result<String> airdrop(@RequestBody String airdropDto){
        log.info("airdrop:{}", airdropDto);
        String sign = Ecdsa.sign(foundationPrivateKey, airdropDto, DataCoder.BASE58);
        return Result.ok(sign);
    }
}
