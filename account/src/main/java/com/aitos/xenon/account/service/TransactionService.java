package com.aitos.xenon.account.service;

import com.aitos.xenon.account.api.domain.dto.PoggRewardDto;
import com.aitos.xenon.account.api.domain.dto.TransferDto;
import com.aitos.xenon.account.domain.PoggReportMiner;
import com.aitos.xenon.account.domain.PoggRewardMiner;
import com.aitos.xenon.account.domain.Transaction;
import com.aitos.xenon.block.api.domain.dto.PoggReportDto;
import com.aitos.xenon.core.model.QueryParams;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

public interface TransactionService {
    void transaction(Transaction transaction);

    void transfer(TransferDto transferDto);

    String poggReward(PoggRewardDto poggRewardDto);

    Transaction query(String txHash);

    IPage<Transaction> list(QueryParams queryParams);

    /**
     * 根据Miner address 获取Report列表
     * @param address
     * @return
     */
    IPage<PoggReportMiner> getReportByMinerAddress(PoggReportDto address);

    /**
     * 根据Miner address 获取Reward列表
     * @param address
     * @return
     */
    IPage<PoggRewardMiner> getRewardByMinerAddress(PoggReportDto address);

    public void abstractTransaction(Transaction transaction);

    List<Transaction> getAll();
}
