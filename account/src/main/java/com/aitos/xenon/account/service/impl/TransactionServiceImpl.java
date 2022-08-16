package com.aitos.xenon.account.service.impl;

import com.aitos.blockchain.web3j.Erc20Service;
import com.aitos.xenon.account.api.domain.dto.PoggRewardDetailDto;
import com.aitos.xenon.account.api.domain.dto.PoggRewardDto;
import com.aitos.xenon.account.api.domain.dto.TransferDto;
import com.aitos.xenon.account.domain.Account;
import com.aitos.xenon.account.domain.Transaction;
import com.aitos.xenon.account.mapper.TransactionMapper;
import com.aitos.xenon.account.service.AccountService;
import com.aitos.xenon.account.service.TransactionService;
import com.aitos.xenon.common.crypto.XenonCrypto;
import com.aitos.xenon.core.constant.BusinessConstants;
import com.aitos.xenon.core.exceptions.ServiceException;
import com.aitos.xenon.core.model.QueryParams;
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
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private Erc20Service erc20Service;

    @Autowired
    private AccountService accountService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transaction(Transaction transaction){
        transaction.setStatus(1);
        //TODO 提取数据分别保存
        log.info("---------------------------------------------------------------------------");
        log.info("transaction type:{}",transaction.getTxType());
        log.info("save transaction:{}",JSON.toJSONString(transaction));
        log.info("---------------------------------------------------------------------------");
        abstractTransaction(transaction);
        //
        transactionMapper.save(transaction);
    }

    private void abstractTransaction(Transaction transaction){

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transfer(TransferDto transferDto) {

        try{
            Account account= accountService.findByAddress(transferDto.getPayments().get(0).getPayeeToHex());

            BigDecimal fee = Convert.fromWei(transferDto.getFee().toString(), Convert.Unit.ETHER);
           /* TransactionReceipt transactionReceipt=erc20Service.transfer(We3jUtils.toWeb3Address(account.getAddress()),fee.toBigInteger()).send();

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
    public String poggReward(PoggRewardDto poggRewardDto) {
        try{
            List<String> addressList=new ArrayList<>();
            List<BigInteger> rewardList=new ArrayList<>();

            Map<String, List<PoggRewardDetailDto>> groupBy=poggRewardDto.getRewards().stream().collect(Collectors.groupingBy(PoggRewardDetailDto::getOwnerAddress));
            for (Map.Entry<String, List<PoggRewardDetailDto>> entry : groupBy.entrySet()) {
                String ownerAddress = entry.getKey();
                BigDecimal ownerAmount = entry.getValue().stream()
                        .map(item->item.getAmount()).reduce(BigDecimal::add)
                        .orElse(BigDecimal.ZERO);
                addressList.add(XenonCrypto.getAddress(ownerAddress));

                BigDecimal blanceEther=  Convert.toWei(ownerAmount, Convert.Unit.ETHER);
                rewardList.add(blanceEther.toBigInteger());
            }
            //调用合约发送奖励
            TransactionReceipt transactionReceipt = erc20Service.rewardMiner_multi(addressList, rewardList).send();

            // todo 更新miner账户余额
            accountService.updateEarning(poggRewardDto.getRewards());


            //记录交易
            String data=JSON.toJSONString(poggRewardDto);
            String txHash= DigestUtils.sha256Hex(data);

            Transaction transaction =new Transaction();
            transaction.setData(data);
            transaction.setHash(txHash);
            transaction.setHeight(poggRewardDto.getHeight());
            transaction.setTxType(BusinessConstants.TXType.TX_REWARD_POGG);
            this.transaction(transaction);
            return transactionReceipt.getTransactionHash();
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
