package com.aitos.xenon.account.service.impl;

import com.aitos.xenon.account.api.RemoteIPFSService;
import com.aitos.xenon.account.api.domain.dto.*;
import com.aitos.xenon.account.api.domain.vo.AccountVo;
import com.aitos.xenon.account.api.domain.vo.TransactionToIpfsVo;
import com.aitos.xenon.account.api.domain.vo.TransactionVo;
import com.aitos.xenon.account.domain.AccountReward;
import com.aitos.xenon.account.domain.Transaction;
import com.aitos.xenon.account.domain.TransactionReport;
import com.aitos.xenon.account.mapper.TransactionMapper;
import com.aitos.xenon.account.service.AccountRewardService;
import com.aitos.xenon.account.service.AccountService;
import com.aitos.xenon.account.service.TransactionService;
import com.aitos.xenon.core.constant.BusinessConstants;
import com.aitos.xenon.core.exceptions.ServiceException;
import com.aitos.xenon.core.model.Result;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRewardService accountRewardService;

    @Autowired
    private RemoteIPFSService remoteIPFSService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transaction(Transaction transaction) {
        transaction.setStatus(1);
        transaction.setCreateTime(LocalDateTime.now());
        transactionMapper.save(transaction);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String poggReward(PoggRewardDto poggRewardDto) {
        try {
            //生成交易数据
            String data = JSON.toJSONString(poggRewardDto);
            String txHash = DigestUtils.sha256Hex(data);
            Transaction transactionTemp = query(txHash);
            if(transactionTemp!=null){
                log.info("poggReward.hash.exist={}",txHash);
                return txHash;
            }

            if(poggRewardDto.getRewards().size()>0){
                List<AccountReward>  accountRewardList=new ArrayList<>();
                BigDecimal totalAmount = poggRewardDto.getRewards().stream().map(PoggRewardDetailDto::getAmount).reduce(BigDecimal.ZERO,BigDecimal::add);

                Map<String, List<PoggRewardDetailDto>> groupBy = poggRewardDto.getRewards().stream().collect(Collectors.groupingBy(PoggRewardDetailDto::getOwnerAddress));
                for (Map.Entry<String, List<PoggRewardDetailDto>> entry : groupBy.entrySet()) {
                    String ownerAddress = entry.getKey();
                    BigDecimal ownerAmount = entry.getValue().stream()
                            .map(item -> item.getAmount()).reduce(BigDecimal::add)
                            .orElse(BigDecimal.ZERO);
                    AccountReward  accountReward=new AccountReward();
                    accountReward.setAddress(ownerAddress);
                    accountReward.setAmount(ownerAmount);
                    accountReward.setAccountType(BusinessConstants.AccountType.WALLET);
                    accountReward.setCreateTime(LocalDateTime.now());
                    accountReward.setHash(txHash);
                    accountReward.setHeight(poggRewardDto.getHeight());

                    BigDecimal rewardPercent = ownerAmount.divide(totalAmount, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
                    accountReward.setRewardPercent(rewardPercent);

                    accountRewardList.add(accountReward);
                }
                //奖励明细记录
                poggRewardDto.getRewards().forEach(item->{
                    AccountReward  accountReward=new AccountReward();
                    accountReward.setAddress(item.getAddress());
                    accountReward.setAmount(item.getAmount());
                    accountReward.setAccountType(BusinessConstants.AccountType.MINER);
                    accountReward.setOwnerAddress(item.getOwnerAddress());
                    accountReward.setCreateTime(LocalDateTime.now());
                    accountReward.setHash(txHash);
                    accountReward.setHeight(poggRewardDto.getHeight());
                    BigDecimal rewardPercent = item.getAmount().divide(totalAmount, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
                    accountReward.setRewardPercent(rewardPercent);
                    accountRewardList.add(accountReward);
                });
                accountRewardService.batchSave(accountRewardList);

                // 累计到账户字段上，方便快速查询
                accountService.updateEarning(accountRewardList);
            }
            //记录交易
            Transaction transaction = new Transaction();
            transaction.setData(data);
            transaction.setHash(txHash);
            transaction.setHeight(poggRewardDto.getHeight());
            transaction.setTxType(BusinessConstants.TXType.TX_REWARD_POGG);
            this.transaction(transaction);
            return txHash;
        } catch (Exception e) {
            log.error("poggReward.error:{}", e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Transaction query(String txHash) {
        return transactionMapper.query(txHash);
    }


    @Override
    public IPage<TransactionVo> list(TransactionSearchDto queryParams) {
        Page<TransactionVo> page = new Page<TransactionVo>(queryParams.getOffset(), queryParams.getLimit());
        IPage<TransactionVo> pageResult = transactionMapper.list(page, queryParams);
        return pageResult;
    }

    @Override
    public List<Transaction> getAll() {
        return transactionMapper.getAll();
    }

    @Override
    public IPage<TransactionReport> getTransactionListByOwner(TransactionSearchDto queryParams) {
        Page<TransactionReport> page = new Page<TransactionReport>(queryParams.getOffset(), queryParams.getLimit());
        IPage<TransactionReport> pageResult = transactionMapper.getTransactionListByOwner(page, queryParams);
        return pageResult;
    }

    @Override
    public void reportDataToIpfs() {
        LocalDateTime  startTime=LocalDateTime.now().minusDays(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endTime=startTime.withHour(23).withMinute(59).withSecond(59);

        AccountSearchDto accountSearchDto=new AccountSearchDto();
        accountSearchDto.setOffset(1);
        accountSearchDto.setLimit(100);
        accountSearchDto.setOffset(1);
        accountSearchDto.setAccountType(BusinessConstants.AccountType.MINER);
        long pages = 1;
        while (accountSearchDto.getOffset()<=pages){
            IPage<AccountVo> list = accountService.list(accountSearchDto);
            pages = list.getPages();
            if(list.getRecords().size()>0){
                for (AccountVo accountVo : list.getRecords()) {
                   List<TransactionToIpfsVo> transactionVoList= transactionMapper.findReportData(accountVo.getAddress(),startTime,endTime);
                   if(transactionVoList.size()>0){
                       IPFSPutDto iPFSPutDto=new IPFSPutDto();
                       iPFSPutDto.setDate(startTime.toLocalDate());
                       iPFSPutDto.setOwnerAddress(transactionVoList.get(0).getOwnerAddress());
                       iPFSPutDto.setMinerAddress(accountVo.getAddress());
                       iPFSPutDto.setData(transactionVoList);

                       log.info("ipfs.push.params={}",JSON.toJSONString(iPFSPutDto));
                       Result result = remoteIPFSService.putRECData(iPFSPutDto);
                       log.info("ipfs.push.result={}",result);
                   }
                }
                accountSearchDto.setOffset(accountSearchDto.getOffset()+1);
            }else{
                break;
            }
        }
    }

    @Override
    public List<String> findHashByHeight(Long height) {
        return transactionMapper.findHashByHeight(height);
    }
}
