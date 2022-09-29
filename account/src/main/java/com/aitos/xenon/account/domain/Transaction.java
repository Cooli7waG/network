package com.aitos.xenon.account.domain;

import com.aitos.xenon.core.model.BaseModel;
import lombok.Data;

@Data
public class Transaction extends BaseModel {

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
    private String ownerAddress;
}
