package com.aitos.xenon.account.controller;

import com.aitos.xenon.account.api.domain.dto.AccountRegisterDto;
import com.aitos.xenon.account.api.domain.dto.AccountSearchDto;
import com.aitos.xenon.account.api.domain.vo.AccountVo;
import com.aitos.xenon.account.domain.Account;
import com.aitos.xenon.account.service.AccountService;
import com.aitos.xenon.core.constant.ApiStatus;
import com.aitos.xenon.core.model.Page;
import com.aitos.xenon.core.model.QueryParams;
import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.core.utils.BeanConvertor;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.BigInteger;


@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/register")
    public Result<Long> register(@RequestBody AccountRegisterDto accountRegister){
        Account accountTemp=accountService.findByAddress(accountRegister.getAddress());
        if(accountTemp!=null){
            return Result.failed(ApiStatus.BUSINESS_ACCOUNT_EXISTED);
        }
        Account account= BeanConvertor.toBean(accountRegister,Account.class);
        accountService.save(account);
        return Result.ok(account.getId());
    }

    @GetMapping("/findByAddress/{address}")
    public Result<AccountVo> findByAddress(@PathVariable("address") String address){
        Account accountTemp=accountService.findByAddress(address);
        AccountVo account= BeanConvertor.toBean(accountTemp,AccountVo.class);
        return Result.ok(account);
    }

    @GetMapping("/balance/{address}")
    public Result<String> getBalance(@PathVariable("address") String address){
        Account accountTemp=accountService.findByAddress(address);
        return Result.ok(accountTemp.getBalance());
    }

    @GetMapping("/nonce/{address}")
    public Result<Long> getNonce(@PathVariable("address") String address){
        Account accountTemp=accountService.findByAddress(address);
        if(accountTemp==null){
            return Result.failed(ApiStatus.BUSINESS_ACCOUNT_NOT_EXIST);
        }
        return Result.ok(accountTemp.getNonce());
    }

    @GetMapping("/list")
    public Result<Page<AccountVo>> list(AccountSearchDto accountSearchDto){
        IPage<AccountVo> listPage= accountService.list(accountSearchDto);
        Page<AccountVo> page=new Page<>(listPage.getTotal(),listPage.getRecords());
        return Result.ok(page);
    }

    @GetMapping("/bmtCirculation")
    public Result<String> bmtCirculation(){
        BigInteger value= accountService.bmtCirculation();
        return Result.ok(value.toString());
    }
}
