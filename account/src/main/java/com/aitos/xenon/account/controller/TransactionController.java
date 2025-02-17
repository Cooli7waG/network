package com.aitos.xenon.account.controller;

import com.aitos.common.crypto.coder.DataCoder;
import com.aitos.common.crypto.ecdsa.Ecdsa;
import com.aitos.xenon.account.api.domain.dto.PoggRewardDto;
import com.aitos.xenon.account.api.domain.dto.TransactionDto;
import com.aitos.xenon.account.api.domain.dto.TransactionSearchDto;
import com.aitos.xenon.account.api.domain.dto.TransferDto;
import com.aitos.xenon.account.api.domain.vo.TransactionVo;
import com.aitos.xenon.account.domain.Transaction;
import com.aitos.xenon.account.domain.TransactionReport;
import com.aitos.xenon.account.service.TransactionService;
import com.aitos.xenon.core.constant.ApiStatus;
import com.aitos.xenon.core.model.Page;
import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.core.utils.BeanConvertor;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Value("${foundation.publicKey}")
    private String foundationPublicKey;

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transaction")
    public Result transaction(@RequestBody TransactionDto transactionDto){
        Transaction  transaction=BeanConvertor.toBean(transactionDto,Transaction.class);
        transactionService.transaction(transaction);
        return Result.ok();
    }


    @PostMapping("/poggReward")
    public Result<String> poggReward(@RequestBody PoggRewardDto poggRewardDto){
        log.info("poggReward.params={}",JSON.toJSONString(poggRewardDto));
        String txHash=transactionService.poggReward(poggRewardDto);
        return Result.ok(txHash);
    }

    @GetMapping("/query")
    public Result<TransactionVo> query(String txHash){
        Transaction transaction=transactionService.query(txHash);
        TransactionVo transactionVo= BeanConvertor.toBean(transaction,TransactionVo.class);
        return Result.ok(transactionVo);
    }

    @GetMapping("/list")
    public Result<Page<TransactionVo>> list(TransactionSearchDto queryParams){
        IPage<TransactionVo> listPage= transactionService.list(queryParams);
        List<TransactionVo>  listVo=BeanConvertor.toList(listPage.getRecords(),TransactionVo.class);
        Page<TransactionVo> page=new Page<>(listPage.getTotal(),listVo);
        return Result.ok(page);
    }

    @GetMapping("/getTransactionListByOwner")
    public Result<Page<TransactionReport>> getTransactionListByOwner(TransactionSearchDto queryParams){
        IPage<TransactionReport> listPage= transactionService.getTransactionListByOwner(queryParams);
        List<TransactionReport>  listVo=BeanConvertor.toList(listPage.getRecords(),TransactionReport.class);
        Page<TransactionReport> page=new Page<>(listPage.getTotal(),listVo);
        return Result.ok(page);
    }

    @GetMapping("/findHashByHeight")
    public Result<List<String>> findHashByHeight(@RequestParam("height") Long height){
        List<String> hashList= transactionService.findHashByHeight(height);
        return Result.ok(hashList);
    }
}
