package com.aitos.xenon.device.domain;

import com.aitos.xenon.core.model.BaseModel;
import lombok.Data;

@Data
public class DeviceDetial extends BaseModel {

    /**
     * Onboard入网块高
     */
    private Long startHeight;

    /**
     * 0=坐标类型位置，1=H3索引类型位置
     */
    private Integer locationType;
    /**
     *
     */
    private Double latitude;

    private Double longitude;

    private Long h3index;

    /**
     * 0=光伏，1=风能，2=水能
     */
    private Integer energy;

    /**
     * 设备能力
     */
    private String capabilities;

    /**
     * 设备型号
     */
    private String deviceModel;

    /**
     * 设备序列号
     */
    private String deviceSerialNum;

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
    private Float sBVoltage;

    /**
     * 蓄电池电压
     */
    private Float sBCurrent;

    /**
     * 蓄电池功率（低）
     */
    private Float sBPowerLow;

    /**
     * 蓄电池功率（高）
     */
    private Float sBPowerHigh;

    /**
     * 负载电压
     */
    private Float sLVoltage;

    /**
     * 负载电流
     */
    private Float sLCurrent;
    /**
     * 负载功率（低）
     */
    private Float sLPowerLow;

    /**
     *负载功率（高）
     */
    private Float sLPowerHigh;

    /**
     * 总累计充电量
     */
    private Float totalChargeVol;

    /**
     * 总累计用电量
     */
    private Float totalUsageVol;

    /**
     * 当日累计充电量
     */
    private Float intervalChargeVol;

    /**
     * 当日累计用电量
     */
    private Float intervalUsageVol;

    private Long deviceId;
}
