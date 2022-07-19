package com.aitos.xenon.account.api;


import com.aitos.xenon.account.api.domain.dto.BatchRewardMinersDto;
import com.aitos.xenon.account.api.domain.dto.TransactionDto;
import com.aitos.xenon.account.api.domain.dto.TransferDto;
import com.aitos.xenon.account.api.domain.vo.TransactionVo;
import com.aitos.xenon.core.model.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "account")
public interface RemoteTransactionService {

    /**
     * 交易
     * @param transactionDto
     * @return
     */
    @PostMapping("/transaction/transaction")
    Result transaction(@RequestBody TransactionDto transactionDto);

    /**
     * 转账
     * @param transferDto
     * @return
     */
    @PostMapping("/transaction/transfer")
    Result transfer(@RequestBody TransferDto transferDto);

    @PostMapping("/transaction/batchRewardMiners")
    Result batchRewardMiners (@RequestBody List<BatchRewardMinersDto> batchRewardMinersDtoList);

    /**
     * 根据hash查询交易
     * @param txHash
     * @return
     */
    @GetMapping("/transaction/query")
    Result<TransactionVo> query(@RequestParam("txHash") String txHash);
}
