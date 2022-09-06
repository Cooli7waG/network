package com.aitos.xenon.account.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author xymoc
 */
@Data
public class TransactionReport {
    /**
     * 主键
     */
    private Long id;
    /**
     * 交易hash
     */
    private String hash;
    /**
     * 交易类型，参考主表
     */
    private Integer txType;
    /**
     * 区块高度
     */
    private Long height;
    /**
     * 交易时间
     */
    private Date createTime;
}
