package com.aitos.xenon.device.api.domain.dto;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.Data;

import java.math.BigInteger;

@Data
@JSONType(orders = {"version","minerAddress","ownerAddress","location"
        ,"minerInfo","stakingFee","minerSignature","payerAddress","payerSignature"},
        ignores = {"ownerSignature","deviceId","txHash","txData"})
public class DeviceBindDto {
    private Integer version;
    /**
     * Miner账户地址
     */
    private String minerAddress;
    /**
     * Onboard费用
     */
    private String ownerAddress;
    /**
     * 支付交易费用和Staking费用的账户地址
     */
    private String payerAddress;

    private DeviceLocationDto location;

    private DeviceInfoDto minerInfo;

    /**
     * Onboard费用
     */
    private BigInteger stakingFee;

    /**
     *staking_fee及以上内容的Miner签名
     */
    private String minerSignature;
    /**
     *staking_fee及以上内容的Payer签名
     */
    private String payerSignature;

    private Long deviceId;

    private String txHash;
    private String txData;

}
