package com.aitos.xenon.account.service;

import com.aitos.xenon.account.api.domain.dto.PoggRewardDto;
import com.aitos.xenon.account.api.domain.dto.TransactionSearchDto;
import com.aitos.xenon.account.api.domain.dto.TransferDto;
import com.aitos.xenon.account.domain.Transaction;
import com.aitos.xenon.account.domain.TransactionReport;
import com.aitos.xenon.core.model.QueryParams;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

public interface TransactionService {
    void transaction(Transaction transaction);

    void transfer(TransferDto transferDto);

    String poggReward(PoggRewardDto poggRewardDto);

    Transaction query(String txHash);

    IPage<Transaction> listOld(QueryParams queryParams);

    IPage<TransactionReport> list(TransactionSearchDto queryParams);

    List<Transaction> getAll();


}
