package com.aitos.xenon.account.domain;

import lombok.Data;

@Data
public class TxWithdraw {

    private String withDrawReq;

    private String metaTx;

    private String withdrawTxSig;
}
