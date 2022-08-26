package com.aitos.xenon.account.controller;

import com.aitos.common.crypto.coder.Base58;
import com.aitos.common.crypto.coder.DataCoder;
import com.aitos.common.crypto.ecdsa.Ecdsa;
import com.aitos.xenon.account.api.domain.dto.*;
import com.aitos.xenon.account.api.domain.vo.TransactionVo;
import com.aitos.xenon.account.domain.PoggReportMiner;
import com.aitos.xenon.account.domain.PoggRewardMiner;
import com.aitos.xenon.account.domain.Transaction;
import com.aitos.xenon.account.service.TransactionService;
import com.aitos.xenon.block.api.domain.dto.PoggReportDto;
import com.aitos.xenon.core.constant.ApiStatus;
import com.aitos.xenon.core.model.Page;
import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.core.utils.BeanConvertor;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Hex;
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

    @GetMapping("/list")
    public Result<Page<TransactionVo>> list(TransactionSearchDto queryParams){
        IPage<Transaction> listPage= transactionService.list(queryParams);

        List<TransactionVo>  listVo=BeanConvertor.toList(listPage.getRecords(),TransactionVo.class);
        Page<TransactionVo> page=new Page<>(listPage.getTotal(),listVo);
        return Result.ok(page);
    }

    @PostMapping("/getReport")
    public Result<Page<PoggReportMiner>> getReportByMinerAddress(@RequestBody PoggReportDto queryParams){
        log.info("getReport PoggReportDto:{}",JSON.toJSONString(queryParams));
        IPage<PoggReportMiner> listPage= transactionService.getReportByMinerAddress(queryParams);
        Page<PoggReportMiner> poggReportPage=new Page<PoggReportMiner>(listPage.getTotal(),listPage.getRecords());
        return Result.ok(poggReportPage);
    }

    @PostMapping("/getReward")
    public Result<Page<PoggRewardMiner>> getRewardByMinerAddress(@RequestBody PoggReportDto queryParams){
        IPage<PoggRewardMiner> listPage= transactionService.getRewardByMinerAddress(queryParams);
        Page<PoggRewardMiner> poggRewardMinerPage=new Page<PoggRewardMiner>(listPage.getTotal(),listPage.getRecords());
        return Result.ok(poggRewardMinerPage);
    }
}
