package com.aitos.xenon.account.api.domain.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class AccountWithdrawDto  {

    @NotEmpty(message = "address is required ")
    private String address;
    @NotNull(message = "amount is required ")
    private String amount;
    @NotNull(message = "requestSig is required ")
    private String requestSig;

    @JSONField(serialize = false)
    public BigDecimal getAmountToDecimal() {
        return new BigDecimal(amount);
    }
}
