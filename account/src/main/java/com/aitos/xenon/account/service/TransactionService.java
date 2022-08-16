package com.aitos.xenon.account.service;

import com.aitos.xenon.account.api.domain.dto.PoggRewardDto;
import com.aitos.xenon.account.api.domain.dto.TransferDto;
import com.aitos.xenon.account.domain.Transaction;
import com.aitos.xenon.core.model.QueryParams;
import com.baomidou.mybatisplus.core.metadata.IPage;

public interface TransactionService {
    void transaction(Transaction transaction);

    void transfer(TransferDto transferDto);

    String poggReward(PoggRewardDto poggRewardDto);

    Transaction query(String txHash);

    IPage<Transaction> list(QueryParams queryParams);
}
