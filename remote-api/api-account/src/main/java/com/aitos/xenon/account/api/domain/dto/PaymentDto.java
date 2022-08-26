package com.aitos.xenon.account.api.domain.dto;


import com.aitos.common.crypto.coder.Base58;
import lombok.Data;
import org.bouncycastle.util.encoders.Hex;

import java.math.BigDecimal;

@Data
public class PaymentDto {

    private Integer version;
    /**
     * 转入账户地址
     */
    private String payee;
    /**
     * 转入金额
     */
    private BigDecimal amount;
    /**
     * 备注信息，长度不超过100字节
     */
    private String memo;

    public String getPayeeToHex(){
        byte[] addressBytes= Base58.decode(payee);
        return Hex.toHexString(addressBytes);
    }
}
