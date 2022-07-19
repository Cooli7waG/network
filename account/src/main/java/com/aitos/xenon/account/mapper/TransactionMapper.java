package com.aitos.xenon.account.mapper;

import com.aitos.xenon.account.domain.Transaction;
import com.aitos.xenon.core.model.QueryParams;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

public interface TransactionMapper {
    void save(Transaction transaction);

    Transaction query(@Param("txHash") String txHash);

    IPage<Transaction> list(Page<Transaction> page, @Param("queryParams") QueryParams queryParams);
}
