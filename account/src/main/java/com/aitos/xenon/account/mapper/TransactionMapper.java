package com.aitos.xenon.account.mapper;

import com.aitos.xenon.account.api.domain.dto.TransactionSearchDto;
import com.aitos.xenon.account.api.domain.vo.TransactionVo;
import com.aitos.xenon.account.domain.Transaction;
import com.aitos.xenon.account.domain.TransactionReport;
import com.aitos.xenon.core.model.QueryParams;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xymoc
 */
@Mapper
public interface TransactionMapper {
    void save(Transaction transaction);

    Transaction query(@Param("txHash") String txHash);

    IPage<TransactionVo> list(Page<TransactionVo> page, @Param("queryParams") TransactionSearchDto queryParams);

    List<Transaction> getAll();

    void saveTransactionReport(TransactionReport transactionReport);

    IPage<TransactionReport> getTransactionListByOwner(Page<TransactionReport> page, @Param("queryParams") TransactionSearchDto queryParams);
}
