package com.aitos.xenon.account.api.domain.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class TransactionDto  {

    /**
     * 交易所在块高
     */
    private Long height;

    /**
     * 交易报文的SHA256的base58编码
     */
    private String hash;

    /**
     * 交易内容，参考数据字典Transactions
     */
    private String data;

    private Integer status;

    private Integer txType;

    private String fromAddress;

    private Long accountId;
}
