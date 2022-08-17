package com.aitos.xenon.account.service.impl;

import com.aitos.blockchain.web3j.Erc20Service;
import com.aitos.xenon.account.api.domain.dto.PoggRewardDetailDto;
import com.aitos.xenon.account.api.domain.dto.PoggRewardDto;
import com.aitos.xenon.account.api.domain.dto.TransferDto;
import com.aitos.xenon.account.domain.Account;
import com.aitos.xenon.account.domain.PoggReportMiner;
import com.aitos.xenon.account.domain.PoggRewardMiner;
import com.aitos.xenon.account.domain.Transaction;
import com.aitos.xenon.account.mapper.TransactionMapper;
import com.aitos.xenon.account.service.AccountService;
import com.aitos.xenon.account.service.TransactionService;
import com.aitos.xenon.block.api.domain.dto.PoggGreenDataDto;
import com.aitos.xenon.block.api.domain.dto.PoggReportDto;
import com.aitos.xenon.common.crypto.XenonCrypto;
import com.aitos.xenon.core.constant.BusinessConstants;
import com.aitos.xenon.core.exceptions.ServiceException;
import com.aitos.xenon.core.model.QueryParams;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import java.math.RoundingMode;
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
        abstractTransaction(transaction);
        transactionMapper.save(transaction);
    }

    @Override
    public void abstractTransaction(Transaction transaction){
        log.info("---------------------------------------------------------------------------");
        log.info("save transaction:{}",JSON.toJSONString(transaction));
        if(BusinessConstants.TXType.TX_REPORT_POGG == transaction.getTxType()){
            log.info("transaction type:{} = {}",transaction.getTxType(),"TX_REPORT_POGG");
            //
            PoggReportDto poggReportDto = JSON.parseObject(transaction.getData(), PoggReportDto.class);
            List<PoggGreenDataDto> decode = PoggGreenDataDto.decode(poggReportDto.getDataList());
            PoggReportMiner report = new PoggReportMiner();
            report.setHeight(transaction.getHeight());
            report.setHash(transaction.getHash());
            for (PoggGreenDataDto poggGreenDataDto : decode) {
                if(report.getTimestamp()==null || poggGreenDataDto.getTimestamp()>report.getTimestamp()){
                    report.setPower(poggGreenDataDto.getPower());
                    report.setTotal(poggGreenDataDto.getTotal());
                    report.setTimestamp(poggGreenDataDto.getTimestamp());
                    report.setAddress(poggReportDto.getAddress());
                }
            }
            transactionMapper.saveReport(report);
        }else if(BusinessConstants.TXType.TX_REWARD_POGG == transaction.getTxType()){
            log.info("transaction type:{} = {}",transaction.getTxType(),"TX_REWARD_POGG");
            List<PoggRewardMiner> rewardList = new ArrayList<>();
            PoggRewardDto poggRewardDto = JSON.parseObject(transaction.getData(), PoggRewardDto.class);
            BigDecimal totalAmount = new BigDecimal("0");
            for (PoggRewardDetailDto poggRewardDtoReward : poggRewardDto.getRewards()) {
                PoggRewardMiner reward = new PoggRewardMiner();
                reward.setHeight(transaction.getHeight());
                reward.setHash(transaction.getHash());
                reward.setAddress(poggRewardDtoReward.getAddress());
                reward.setAmount(poggRewardDtoReward.getAmount());
                totalAmount = totalAmount.add(poggRewardDtoReward.getAmount());
                reward.setOwnerAddress(poggRewardDtoReward.getOwnerAddress());
                rewardList.add(reward);
            }
            //计算百分比并入库
            for (PoggRewardMiner rewardMiner : rewardList) {
                BigDecimal divide = rewardMiner.getAmount().divide(totalAmount, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
                rewardMiner.setRewardPercent(divide.toString());
                transactionMapper.saveReward(rewardMiner);
            }
        }else {
            log.info("transaction type:{} = ?",transaction.getTxType());
        }
        log.info("---------------------------------------------------------------------------");
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

    @Override
    public IPage<PoggReportMiner> getReportByMinerAddress(PoggReportDto queryParams) {
        Page<PoggReportMiner> page=new Page<PoggReportMiner>(queryParams.getOffset(),queryParams.getLimit());
        IPage<PoggReportMiner> pageResult=transactionMapper.getReportByMinerAddress(page,queryParams);
        return pageResult;
    }

    @Override
    public IPage<PoggRewardMiner> getRewardByMinerAddress(PoggReportDto queryParams) {
        Page<PoggRewardMiner> page=new Page<PoggRewardMiner>(queryParams.getOffset(),queryParams.getLimit());
        IPage<PoggRewardMiner> pageResult=transactionMapper.getRewardByMinerAddress(page,queryParams);
        return pageResult;
    }

    @Override
    public List<Transaction> getAll() {
        return transactionMapper.getAll();
    }
}
