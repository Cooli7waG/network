package com.aitos.xenon.account.domain;

import com.aitos.xenon.account.api.domain.dto.AccountWithdrawDto;
import com.aitos.xenon.account.api.domain.vo.RemoteKMSSignVo;
import lombok.Data;

@Data
public class TxWithdraw {

    private AccountWithdrawDto withDrawReq;

    private Object metaTx;

    private RemoteKMSSignVo withdrawTxSig;
}
