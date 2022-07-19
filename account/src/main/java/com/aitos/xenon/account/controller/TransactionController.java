package com.aitos.xenon.account.controller;

import com.aitos.xenon.account.api.domain.dto.BatchRewardMinersDto;
import com.aitos.xenon.account.api.domain.dto.TransactionDto;
import com.aitos.xenon.account.api.domain.dto.TransactionSearchDto;
import com.aitos.xenon.account.api.domain.dto.TransferDto;
import com.aitos.xenon.account.api.domain.vo.AccountVo;
import com.aitos.xenon.account.api.domain.vo.TransactionVo;
import com.aitos.xenon.account.domain.Transaction;
import com.aitos.xenon.account.service.TransactionService;
import com.aitos.xenon.common.crypto.ed25519.Base58;
import com.aitos.xenon.common.crypto.ed25519.Ed25519;
import com.aitos.xenon.core.constant.ApiStatus;
import com.aitos.xenon.core.constant.BusinessConstants;
import com.aitos.xenon.core.model.Page;
import com.aitos.xenon.core.model.QueryParams;
import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.core.utils.BeanConvertor;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/transaction")
public class TransactionController {

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
        byte[] signatureBytes= Base58.decode(transferDto.getSignature());
        byte[] addressBytes= Base58.decode(transferDto.getPayments().get(0).getPayee());
        Boolean verify= Ed25519.verify(BusinessConstants.ArgonFoundation.publicKey,addressBytes,signatureBytes);
        if(!verify){
            return Result.failed(ApiStatus.BUSINESS_TRANSACTION_SIGN_ERROR);
        }
        transferDto.setTxData(JSON.toJSONString(transferDto));
        transactionService.transfer(transferDto);
        return Result.ok();
    }

    @PostMapping("/batchRewardMiners")
    public Result batchRewardMiners(@RequestBody List<BatchRewardMinersDto> batchRewardMinersDtoList){
        transactionService.batchRewardMiners(batchRewardMinersDtoList);
        return Result.ok();
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
}
