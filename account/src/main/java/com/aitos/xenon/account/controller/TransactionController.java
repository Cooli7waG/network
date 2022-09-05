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

    @PostMapping("/transfer")
    public Result transfer(@RequestBody TransferDto transferDto){
        Boolean verify= Ecdsa.verifyByAddress(foundationPublicKey,transferDto.getPayments().get(0).getPayee(),transferDto.getSignature(), DataCoder.BASE58);
        if(!verify){
            return Result.failed(ApiStatus.BUSINESS_TRANSACTION_SIGN_ERROR);
        }
        transferDto.setTxData(JSON.toJSONString(transferDto));
        transactionService.transfer(transferDto);
        return Result.ok();
    }

    @PostMapping("/poggReward")
    public Result<String> poggReward(@RequestBody PoggRewardDto poggRewardDto){
        String txHash=transactionService.poggReward(poggRewardDto);
        return Result.ok(txHash);
    }

    @GetMapping("/query")
    public Result<TransactionVo> query(String txHash){
        Transaction transaction=transactionService.query(txHash);
        TransactionVo transactionVo= BeanConvertor.toBean(transaction,TransactionVo.class);
        return Result.ok(transactionVo);
    }

    @GetMapping("/listOld")
    public Result<Page<TransactionVo>> listOld(TransactionSearchDto queryParams){
        IPage<Transaction> listPage= transactionService.listOld(queryParams);
        List<TransactionVo>  listVo=BeanConvertor.toList(listPage.getRecords(),TransactionVo.class);
        Page<TransactionVo> page=new Page<>(listPage.getTotal(),listVo);
        return Result.ok(page);
    }

    @GetMapping("/list")
    public Result<Page<TransactionReport>> list(TransactionSearchDto queryParams){
        IPage<TransactionReport> listPage= transactionService.list(queryParams);
        List<TransactionReport>  listVo=BeanConvertor.toList(listPage.getRecords(),TransactionReport.class);
        Page<TransactionReport> page=new Page<>(listPage.getTotal(),listVo);
        return Result.ok(page);
    }

}
