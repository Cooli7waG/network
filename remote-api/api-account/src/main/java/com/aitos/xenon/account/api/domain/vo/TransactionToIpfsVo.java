package com.aitos.xenon.account.api.domain.vo;

import com.aitos.xenon.core.json.LocalDateTimeSerializer;
import com.aitos.xenon.core.utils.JacksonCustomizerConfig;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionToIpfsVo {

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

    private Integer txType;

    @JSONField(serialize = false)
    private String fromAddress;
    @JSONField(serialize = false)
    private String ownerAddress;

    @JSONField(serializeUsing = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;

}
