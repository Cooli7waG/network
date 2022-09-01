package com.aitos.xenon.account.service.impl;

import com.aitos.blockchain.web3j.Erc20Service;
import com.aitos.xenon.account.api.domain.dto.PoggRewardDetailDto;
import com.aitos.xenon.account.api.domain.dto.PoggRewardDto;
import com.aitos.xenon.account.api.domain.dto.TransactionSearchDto;
import com.aitos.xenon.account.api.domain.dto.TransferDto;
import com.aitos.xenon.account.api.domain.vo.AccountVo;
import com.aitos.xenon.account.domain.*;
import com.aitos.xenon.account.mapper.TransactionMapper;
import com.aitos.xenon.account.service.AccountService;
import com.aitos.xenon.account.service.TransactionService;
import com.aitos.xenon.block.api.domain.dto.PoggGreenDataDto;
import com.aitos.xenon.block.api.domain.dto.PoggReportDto;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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
    public void transaction(Transaction transaction) {
        transaction.setStatus(1);
        //TODO 提取数据分别保存
        if(!transaction.getData().equals("[]")) {
            abstractTransaction(transaction);
            abstractTransactionReport(transaction);
        }
        transaction.setCreateTime(LocalDateTime.now());
        transactionMapper.save(transaction);
    }

    private void abstractTransactionReport(Transaction transaction){
        try{
            TransactionReport transactionReport = new TransactionReport();
            transactionReport.setHash(transaction.getHash());
            transactionReport.setTxType(transaction.getTxType());
            transactionReport.setHeight(transaction.getHeight());
            transactionReport.setCreateTime(new Date());
            JSONObject jsonObject = JSON.parseObject(transaction.getData());
            if (BusinessConstants.TXType.TX_REGISTER_MINER == transaction.getTxType()) {
                transactionReport.setMiner(jsonObject.getString(BusinessConstants.TxAddressType.ADDRESS));
            }else if(BusinessConstants.TXType.TX_ONBOARD_MINER == transaction.getTxType()){
                transactionReport.setMiner(jsonObject.getString(BusinessConstants.TxAddressType.MINER_ADDRESS));
                transactionReport.setOwner(jsonObject.getString(BusinessConstants.TxAddressType.OWNER_ADDRESS));
            }else if(BusinessConstants.TXType.TX_TRANSFER_MINER == transaction.getTxType()){
                transactionReport.setMiner(jsonObject.getString(BusinessConstants.TxAddressType.MINER));
                transactionReport.setOwner(jsonObject.getString(BusinessConstants.TxAddressType.OWNER));
            }else if(BusinessConstants.TXType.TX_TERMINATE_MINER == transaction.getTxType()){
                transactionReport.setMiner(jsonObject.getString(BusinessConstants.TxAddressType.MINER));
            }else if(BusinessConstants.TXType.TX_AIRDROP_MINER == transaction.getTxType()){
                transactionReport.setMiner(jsonObject.getString(BusinessConstants.TxAddressType.MINER_ADDRESS));
                transactionReport.setOwner(jsonObject.getString(BusinessConstants.TxAddressType.OWNER_ADDRESS));
            }else if(BusinessConstants.TXType.TX_CLAIM_MINER == transaction.getTxType()){
                transactionReport.setMiner(jsonObject.getString(BusinessConstants.TxAddressType.MINER_ADDRESS));
                transactionReport.setOwner(jsonObject.getString(BusinessConstants.TxAddressType.OWNER_ADDRESS));
            }else if(BusinessConstants.TXType.TX_COMMIT_POGG == transaction.getTxType()){

            }else if(BusinessConstants.TXType.TX_REPORT_POGG == transaction.getTxType()){
                transactionReport.setMiner(jsonObject.getString(BusinessConstants.TxAddressType.ADDRESS));
            }else if(BusinessConstants.TXType.TX_REWARD_POGG == transaction.getTxType()){
                PoggRewardDto poggRewardDto = JSON.parseObject(transaction.getData(), PoggRewardDto.class);
                for (PoggRewardDetailDto poggRewardDtoReward : poggRewardDto.getRewards()) {
                    transactionReport.setMiner(poggRewardDtoReward.getAddress());
                    transactionReport.setOwner(poggRewardDtoReward.getOwnerAddress());
                }
            }
            //TODO 是否需要通过miner address查询出owner address

            //数据入库
            transactionReport.setCreateTime(new Date());
            transactionMapper.saveTransactionReport(transactionReport);
        }catch (Exception e){
            log.error("abstractTransactionReport error:{}",e.getMessage());
        }
    }

    @Override
    public void abstractTransaction(Transaction transaction) {
        try{
            if (BusinessConstants.TXType.TX_REPORT_POGG == transaction.getTxType()) {
                log.info("transaction type:{} = {}", transaction.getTxType(), "TX_REPORT_POGG");
                //
                PoggReportDto poggReportDto = JSON.parseObject(transaction.getData(), PoggReportDto.class);
                List<PoggGreenDataDto> decode = PoggGreenDataDto.decode(poggReportDto.getDataList());
                PoggReportMiner report = new PoggReportMiner();
                report.setHeight(transaction.getHeight());
                report.setHash(transaction.getHash());
                for (PoggGreenDataDto poggGreenDataDto : decode) {
                    if (report.getTimestamp() == null || poggGreenDataDto.getTimestamp() > report.getTimestamp()) {
                        report.setPower(poggGreenDataDto.getPower());
                        report.setTotal(poggGreenDataDto.getTotal());
                        report.setTimestamp(poggGreenDataDto.getTimestamp());
                        report.setAddress(poggReportDto.getAddress());
                    }
                }
                report.setCreateTime(new Date());
                transactionMapper.saveReport(report);
            } else if (BusinessConstants.TXType.TX_REWARD_POGG == transaction.getTxType()) {
                log.info("transaction type:{} = {}", transaction.getTxType(), "TX_REWARD_POGG");
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
                    rewardMiner.setCreateTime(new Date());
                    transactionMapper.saveReward(rewardMiner);
                }
            } else {
                log.info("transaction type:{} = ?", transaction.getTxType());
            }
        }catch (Exception e){
            log.error("abstractTransaction error:{}",e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transfer(TransferDto transferDto) {

        try {
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
        } catch (Exception e) {
            log.error("transfer.error:{}", e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String poggReward(PoggRewardDto poggRewardDto) {
        try {
            List<String> addressList = new ArrayList<>();
            List<BigInteger> rewardList = new ArrayList<>();

            if(poggRewardDto.getRewards().size()>0){
                Map<String, List<PoggRewardDetailDto>> groupBy = poggRewardDto.getRewards().stream().collect(Collectors.groupingBy(PoggRewardDetailDto::getOwnerAddress));
                for (Map.Entry<String, List<PoggRewardDetailDto>> entry : groupBy.entrySet()) {
                    String ownerAddress = entry.getKey();
                    BigDecimal ownerAmount = entry.getValue().stream()
                            .map(item -> item.getAmount()).reduce(BigDecimal::add)
                            .orElse(BigDecimal.ZERO);
                    addressList.add(ownerAddress);

                    BigDecimal blanceEther = Convert.toWei(ownerAmount, Convert.Unit.ETHER);
                    rewardList.add(blanceEther.toBigInteger());
                }
                //调用合约发送奖励
                TransactionReceipt transactionReceipt = erc20Service.rewardMiner_multi(addressList, rewardList).send();
                log.info("erc20.transactionReceipt={}",transactionReceipt.getTransactionHash());
                // 更新miner账户余额
                accountService.updateEarning(poggRewardDto.getRewards());
            }
            //记录交易
            String data = JSON.toJSONString(poggRewardDto);
            String txHash = DigestUtils.sha256Hex(data);
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
    public IPage<Transaction> listOld(QueryParams queryParams) {
        Page<Transaction> page = new Page<Transaction>(queryParams.getOffset(), queryParams.getLimit());
        IPage<Transaction> pageResult = transactionMapper.listOld(page, queryParams);
        return pageResult;
    }

    @Override
    public IPage<TransactionReport> list(TransactionSearchDto queryParams) {
        Page<TransactionReport> page = new Page<TransactionReport>(queryParams.getOffset(), queryParams.getLimit());
        IPage<TransactionReport> pageResult = transactionMapper.list(page, queryParams);
        return pageResult;
    }

    @Override
    public IPage<PoggReportMiner> getReportByMinerAddress(PoggReportDto queryParams) {
        Page<PoggReportMiner> page = new Page<PoggReportMiner>(queryParams.getOffset(), queryParams.getLimit());
        IPage<PoggReportMiner> pageResult = transactionMapper.getReportByMinerAddress(page, queryParams);
        return pageResult;
    }

    @Override
    public IPage<PoggRewardMiner> getRewardByMinerAddress(PoggReportDto queryParams) {
        Page<PoggRewardMiner> page = new Page<PoggRewardMiner>(queryParams.getOffset(), queryParams.getLimit());
        IPage<PoggRewardMiner> pageResult = transactionMapper.getRewardByMinerAddress(page, queryParams);
        return pageResult;
    }

    @Override
    public List<Transaction> getAll() {
        return transactionMapper.getAll();
    }
}
