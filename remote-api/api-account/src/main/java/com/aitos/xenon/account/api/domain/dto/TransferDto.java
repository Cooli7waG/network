package com.aitos.xenon.account.api.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.apache.commons.codec.digest.DigestUtils;
import java.math.BigDecimal;
import java.util.List;

@Data
public class TransferDto  {

    private Integer version;

    private List<PaymentDto> payments;

    private BigDecimal fee;

    private Long nonce;

    private String signature;

    @JsonIgnore
    private String txHash;
    @JsonIgnore
    private String txData;

    public String getTxHash(){
        return txData!=null? DigestUtils.sha256Hex(txData):null;
    }

}
