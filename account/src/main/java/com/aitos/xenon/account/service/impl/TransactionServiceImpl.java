package com.aitos.xenon.account.service.impl;

import com.aitos.blockchain.web3j.BmtERC20;
import com.aitos.blockchain.web3j.We3jUtils;
import com.aitos.xenon.account.api.domain.dto.BatchRewardMinersDto;
import com.aitos.xenon.account.api.domain.dto.TransferDto;
import com.aitos.xenon.account.domain.Account;
import com.aitos.xenon.account.domain.Transaction;
import com.aitos.xenon.account.mapper.TransactionMapper;
import com.aitos.xenon.account.service.AccountService;
import com.aitos.xenon.account.service.TransactionService;
import com.aitos.xenon.block.api.RemoteBlockService;
import com.aitos.xenon.core.constant.ApiStatus;
import com.aitos.xenon.core.constant.BusinessConstants;
import com.aitos.xenon.core.exceptions.ServiceException;
import com.aitos.xenon.core.model.QueryParams;
import com.aitos.xenon.core.model.Result;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private BmtERC20 bmtERC20;

    @Autowired
    private AccountService accountService;

    @Autowired
    private RemoteBlockService remoteBlockService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transaction(Transaction transaction){
        transaction.setStatus(1);
        transactionMapper.save(transaction);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transfer(TransferDto transferDto) {

        try{
            Account account= accountService.findByAddress(transferDto.getPayments().get(0).getPayeeToHex());

            BigDecimal fee = Convert.fromWei(transferDto.getFee().toString(), Convert.Unit.ETHER);
           /* TransactionReceipt transactionReceipt=bmtERC20.transfer(We3jUtils.toWeb3Address(account.getAddress()),fee.toBigInteger()).send();

            Result<Long> heightResult=remoteBlockService.getBlockHeight();
            if(heightResult.getCode()== ApiStatus.SUCCESS.getCode()){
                Transaction transaction=new Transaction();
                transaction.setHeight(heightResult.getData());
                transaction.setData(transferDto.getTxData());
                transaction.setHash(transferDto.getTxHash());
                transaction.setWeb3TxHash(transactionReceipt.getTransactionHash());
                transaction.setStatus(transactionReceipt.getStatus());
                transactionMapper.save(transaction);

                *//*if(transactionReceipt.getStatus().equals("0x1")){
                    accountService.updateNonce(transferDto.getSignature());
                }*//*
            }*/
        }catch (Exception e){
            log.error("transfer.error:{}",e);
            throw new ServiceException(e.getMessage());
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchRewardMiners(List<BatchRewardMinersDto> batchRewardMinersDtoList) {

        try{
           List<Long>  idsList= batchRewardMinersDtoList.stream().map(item->item.getAccountId()).collect(Collectors.toList());

           List<Account> list=accountService.findListByIds(idsList);


            List<String> addressList=new ArrayList<>();
            List<BigInteger> rewardList=new ArrayList<>();

           //更新账户余额
            batchRewardMinersDtoList.stream().forEach(item->{
               Account accountTemp= list.stream().filter(account->account.getId().equals(item.getAccountId())).findFirst().get();
                addressList.add(accountTemp.getAddress());
                BigDecimal blanceEther=  Convert.toWei(item.getReward(), Convert.Unit.ETHER);
                rewardList.add(blanceEther.toBigInteger());
            });

            //调用合约发送奖励
            bmtERC20.rewardMiner_multi(addressList,rewardList).send();

            //记录交易
            String data=JSON.toJSONString(batchRewardMinersDtoList);
            String txHash= DigestUtils.sha256Hex(data);

            Transaction transaction =new Transaction();
            transaction.setData(data);
            transaction.setHash(txHash);
            transaction.setHeight(batchRewardMinersDtoList.get(0).getHeight());
            transaction.setTxType(BusinessConstants.TXType.TX_REWARD_POGG);
            this.transaction(transaction);
        }catch (Exception e){
            log.error("transfer.error:{}",e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Transaction query(String txHash) {
        return transactionMapper.query(txHash);
    }

    @Override
    public IPage<Transaction> list(QueryParams queryParams) {
        Page<Transaction> page=new Page<Transaction>(queryParams.getOffset(),queryParams.getLimit());
        IPage<Transaction> pageResult=transactionMapper.list(page,queryParams);
        return pageResult;
    }
}
