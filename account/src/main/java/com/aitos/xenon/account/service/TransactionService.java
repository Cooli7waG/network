package com.aitos.xenon.account.service;

import com.aitos.xenon.account.api.domain.dto.PoggRewardDto;
import com.aitos.xenon.account.api.domain.dto.TransactionSearchDto;
import com.aitos.xenon.account.api.domain.dto.TransferDto;
import com.aitos.xenon.account.api.domain.vo.TransactionVo;
import com.aitos.xenon.account.domain.Transaction;
import com.aitos.xenon.account.domain.TransactionReport;
import com.aitos.xenon.core.model.QueryParams;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

public interface TransactionService {
    void transaction(Transaction transaction);

    String poggReward(PoggRewardDto poggRewardDto);

    Transaction query(String txHash);

    IPage<TransactionVo> list(TransactionSearchDto queryParams);

    List<Transaction> getAll();

    IPage<TransactionReport> getTransactionListByOwner(TransactionSearchDto queryParams);

    void reportDataToIpfs();

    List<String> findHashByHeight(Long height);
}
