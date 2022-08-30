package com.aitos.xenon.account.mapper;

import com.aitos.xenon.account.api.domain.dto.TransactionSearchDto;
import com.aitos.xenon.account.domain.PoggReportMiner;
import com.aitos.xenon.account.domain.PoggRewardMiner;
import com.aitos.xenon.account.domain.Transaction;
import com.aitos.xenon.account.domain.TransactionReport;
import com.aitos.xenon.block.api.domain.dto.PoggReportDto;
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

    IPage<Transaction> listOld(Page<Transaction> page, @Param("queryParams") QueryParams queryParams);

    IPage<TransactionReport> list(Page<TransactionReport> page, @Param("queryParams") TransactionSearchDto queryParams);

    /**
     * 保存Report交易
     * @param report
     */
    void saveReport(PoggReportMiner report);

    /**
     * 保存Reward交易
     * @param reward
     */
    void saveReward(PoggRewardMiner reward);

    /**
     * 根据Miner address 获取Report列表
     * @param page
     * @param queryParams
     * @return
     */
    IPage<PoggReportMiner> getReportByMinerAddress(Page<PoggReportMiner> page, @Param("queryParams")PoggReportDto queryParams);

    /**
     * 根据Miner address 获取Reward列表
     * @param page
     * @param queryParams
     * @return
     */
    IPage<PoggRewardMiner> getRewardByMinerAddress(Page<PoggRewardMiner> page, @Param("queryParams")PoggReportDto queryParams);

    List<Transaction> getAll();

    void saveTransactionReport(TransactionReport transactionReport);
}
