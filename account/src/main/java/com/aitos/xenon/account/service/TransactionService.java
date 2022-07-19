package com.aitos.xenon.account.service;

import com.aitos.xenon.account.api.domain.dto.BatchRewardMinersDto;
import com.aitos.xenon.account.api.domain.dto.TransferDto;
import com.aitos.xenon.account.api.domain.vo.TransactionVo;
import com.aitos.xenon.account.domain.Transaction;
import com.aitos.xenon.core.model.QueryParams;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TransactionService {
    void transaction(Transaction transaction);

    void transfer(TransferDto transferDto);

    void batchRewardMiners(List<BatchRewardMinersDto> batchRewardMinersDtoList);

    Transaction query(String txHash);

    IPage<Transaction> list(QueryParams queryParams);
}
