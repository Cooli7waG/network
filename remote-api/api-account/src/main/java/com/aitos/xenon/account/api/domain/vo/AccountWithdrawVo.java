package com.aitos.xenon.account.api.domain.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class AccountWithdrawVo {
    private String metaTx;
    private String contract;
}
