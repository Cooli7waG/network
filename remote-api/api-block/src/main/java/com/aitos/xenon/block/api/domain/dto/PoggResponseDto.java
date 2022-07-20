package com.aitos.xenon.block.api.domain.dto;

import com.aitos.xenon.common.crypto.ed25519.Base58;
import lombok.Data;
import org.bouncycastle.util.encoders.Hex;

@Data
public class PoggResponseDto {
    private Integer version;
    private String random;
    /**
     * Miner私钥对随机数的签名
     */
    private String randomSignature;

    private String address;
    /**
     * 对应的PoGG challenge交易hash
     */
    private String challengeHash;

    /**
     * 发电电压
     */
    private Float sGVoltage;

    /**
     * 发电电流
     */
    private Float sGCurrent;
    /**
     * 发电功率（低）
     */
    private Float sGPowerLow;
    /**
     * 发电功率（高）
     */
    private Float sGPowerHigh;
    /**
     * 蓄电池电压
     */
    private Float  sBVoltage;
    /**
     * 蓄电池电流
     */
    private Float  sBCurrent;
    /**
     * 蓄电池功率（低）
     */
    private Float  sBPowerLow;
    /**
     * 蓄电池功率（高）
     */
    private Float  sBPowerHigh;
    /**
     *负载电压
     */
    private Float  sLVoltage;
    /**
     * 负载电流
     */
    private Float  sLCurrent;
    /**
     * 负载功率（低）
     */
    private Float  sLPowerLow;
    /**
     * 负载功率（高）
     */
    private Float  sLPowerHigh;
    /**
     * 当日累计充电量
     */
    private Float  todayChargeVol;
    /**
     * 当日累计用电量
     */
    private Float  todayUsageVol;
    /**
     * 本期累计充电量
     */
    private Float  intervalChargeVol;
    /**
     * 本期累计用电量
     */
    private Float  intervalUsageVol;

    private String  signature;

}
