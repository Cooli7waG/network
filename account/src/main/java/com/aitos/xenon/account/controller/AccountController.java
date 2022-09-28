package com.aitos.xenon.account.controller;

import com.aitos.common.crypto.coder.DataCoder;
import com.aitos.common.crypto.ecdsa.Ecdsa;
import com.aitos.common.crypto.ecdsa.Hash;
import com.aitos.xenon.account.api.RemoteKMSService;
import com.aitos.xenon.account.api.RemoteTokenService;
import com.aitos.xenon.account.api.domain.dto.*;
import com.aitos.xenon.account.api.domain.vo.*;
import com.aitos.xenon.account.domain.Account;
import com.aitos.xenon.account.domain.Transaction;
import com.aitos.xenon.account.domain.TxWithdraw;
import com.aitos.xenon.account.service.AccountService;
import com.aitos.xenon.account.service.TransactionService;
import com.aitos.xenon.block.api.RemoteBlockService;
import com.aitos.xenon.core.constant.ApiStatus;
import com.aitos.xenon.core.constant.BusinessConstants;
import com.aitos.xenon.core.model.Page;
import com.aitos.xenon.core.model.QueryParams;
import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.core.utils.BeanConvertor;
import com.aitos.xenon.core.utils.MetaMaskUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;


@RestController
@RequestMapping("/account")
@Slf4j
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private RemoteTokenService remoteTokenService;

    @Autowired
    private RemoteKMSService remoteKMSService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private RemoteBlockService remoteBlockService;

    /**
     * 账户注册接口
     * @param accountRegister
     * @return
     */
    @PostMapping("/register")
    public Result<Long> register(@RequestBody AccountRegisterDto accountRegister){
        AccountVo accountTemp=accountService.findByAddress(accountRegister.getAddress());
        if(accountTemp!=null){
            return Result.failed(ApiStatus.BUSINESS_ACCOUNT_EXISTED);
        }
        Account account= BeanConvertor.toBean(accountRegister,Account.class);
        accountService.save(account);
        return Result.ok(account.getId());
    }

    /**
     * 根据账户地址查询账户信息
     * @param address
     * @return
     */
    @GetMapping("/{address}")
    public Result<AccountVo> findByAddress(@PathVariable("address") String address){
        AccountVo accountTemp=accountService.findByAddress(address);
        return Result.ok(accountTemp);
    }

    /**
     * todo  该接口后续会被删除,请使用findByAddress
     * @param address
     * @return
     */
    @Deprecated
    @GetMapping("/findByAddress/{address}")
    public Result<AccountVo> findByAddress2(@PathVariable("address") String address){
        AccountVo accountTemp=accountService.findByAddress(address);
        return Result.ok(accountTemp);
    }
    /**
     * 根据账户地址查询nonce信息
     * @param address
     * @return
     */
    @GetMapping("/nonce/{address}")
    public Result<Long> getNonce(@PathVariable("address") String address){
        AccountVo accountTemp=accountService.findByAddress(address);
        if(accountTemp==null){
            return Result.failed(ApiStatus.BUSINESS_ACCOUNT_NOT_EXIST);
        }
        return Result.ok(accountTemp.getNonce());
    }

    /**
     * 分页查询账户列表
     * @param accountSearchDto
     * @return
     */
    @GetMapping("/list")
    public Result<Page<AccountVo>> list(AccountSearchDto accountSearchDto){
        IPage<AccountVo> listPage= accountService.list(accountSearchDto);
        Page<AccountVo> page=new Page<>(listPage.getTotal(),listPage.getRecords());
        return Result.ok(page);
    }

    @PostMapping("/withdraw")
    public Result<HashMap> withdraw(@Validated @RequestBody AccountWithdrawDto accountWithdrawDto){
        log.info("withdraw.params={}",JSON.toJSONString(accountWithdrawDto));
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        filter.getExcludes().add("requestSig");
        String data=JSON.toJSONString(accountWithdrawDto,filter);
        byte[] message = MetaMaskUtils.getMessage(data);
        if(!Ecdsa.verifyByAddress(accountWithdrawDto.getAddress(),message,accountWithdrawDto.getRequestSig(), DataCoder.BASE58)){
            return Result.failed(ApiStatus.BUSINESS_ACCOUNT_SIGN_ERROR);
        }
        AccountVo accountVo = accountService.findByAddress(accountWithdrawDto.getAddress());
        if(accountVo==null){
            return Result.failed(ApiStatus.BUSINESS_ACCOUNT_NOT_EXIST);
        }else if(accountVo.getEarningMint().compareTo(accountWithdrawDto.getAmountToDecimal())<0){
            return Result.failed(ApiStatus.BUSINESS_ACCOUNT_BLANCE_INSUFFICIENT);
        }
        //获取nonce
        TokenServiceSearchNonceDto tokenServiceSearchNonceDto=new TokenServiceSearchNonceDto();
        tokenServiceSearchNonceDto.setAccount(accountWithdrawDto.getAddress());
        Result<TokenServiceSearchNonceVo> nonceResult = remoteTokenService.fetchNonce(tokenServiceSearchNonceDto);
        log.info("nonceResult={}",JSON.toJSONString(nonceResult));
        Long nonce = accountVo.getNonce();
        if(nonceResult.getCode()!=ApiStatus.SUCCESS.getCode()){
            return Result.failed(ApiStatus.ERROR);
        }else if(!nonceResult.getData().getNonce().equals(nonce)){
            return Result.failed(ApiStatus.BUSINESS_ACCOUNT_NONCE_MISMATCH_PATTERN);
        }

        //发起提现
        TokenServiceSignatureDto tokenServiceSignatureDto=new TokenServiceSignatureDto();
        tokenServiceSignatureDto.setRecipient(accountWithdrawDto.getAddress());
        tokenServiceSignatureDto.setNonce(nonce);
        tokenServiceSignatureDto.setValue(accountWithdrawDto.getAmountToDecimal().multiply(new BigDecimal(10).pow(18)).toBigInteger().toString());
        Result<HashMap> withdrawResult = remoteTokenService.withdraw(tokenServiceSignatureDto);
        log.info("withdrawResult={}",JSON.toJSONString(withdrawResult));
        if(withdrawResult.getCode()!=ApiStatus.SUCCESS.getCode()){
            return Result.failed(ApiStatus.ERROR);
        }


        //kms签名
        JSONObject withDrawReq=(JSONObject)JSON.toJSON(accountWithdrawDto);
        withDrawReq.put("metaTx",withdrawResult.getData());
        String withdrawTxJSON=withDrawReq.toJSONString();
        byte[] hash = Hash.sha3(withdrawTxJSON.getBytes());
        RemoteKMSSignDto remoteKMSSignDto=new RemoteKMSSignDto();
        remoteKMSSignDto.setKeyId(BusinessConstants.TokenKeyId.REWARD);
        remoteKMSSignDto.setHash(Hex.toHexString(hash));
        remoteKMSSignDto.setRawData(withdrawTxJSON);
        Result<RemoteKMSSignVo> signResult = remoteKMSService.sign(remoteKMSSignDto);
        log.info("signResult={}",JSON.toJSONString(signResult));
        if(signResult.getCode()!=ApiStatus.SUCCESS.getCode()){
            return Result.failed(ApiStatus.ERROR);
        }

        //生成交易记录
        TxWithdraw txWithdraw=new TxWithdraw();
        txWithdraw.setWithdrawTxSig(signResult.getData());
        txWithdraw.setWithDrawReq(accountWithdrawDto);
        txWithdraw.setMetaTx(withdrawResult.getData());

        String jsonData = JSON.toJSONString(txWithdraw);
        String txHash = DigestUtils.sha256Hex(jsonData);

        Result<Long> blockHeightResult = remoteBlockService.getBlockHeight();
        log.info("blockHeightResult={}",JSON.toJSONString(blockHeightResult));
        if(blockHeightResult.getCode()!=ApiStatus.SUCCESS.getCode()){
            return Result.failed(ApiStatus.ERROR);
        }

        Transaction transaction=new Transaction();
        transaction.setFromAddress(accountWithdrawDto.getAddress());
        transaction.setHeight(blockHeightResult.getData());
        transaction.setTxType(BusinessConstants.TXType.TX_WITHDRAW);
        transaction.setHash(txHash);
        transaction.setData(jsonData);
        transactionService.transaction(transaction);

        accountService.withdraw(accountWithdrawDto.getAddress(),accountWithdrawDto.getAmountToDecimal(), LocalDateTime.now());

        return Result.ok(withdrawResult.getData());
    }

    private String getJSON(Object data,List<String> includes,List<String> excludes){
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        if(includes!=null&&includes.size()>0){
            filter.getIncludes().addAll(includes);
        }
        if(excludes!=null&&excludes.size()>0){
            filter.getExcludes().addAll(excludes);
        }
        String jsonData=JSON.toJSONString(data,filter);
        return jsonData;
    }
}
