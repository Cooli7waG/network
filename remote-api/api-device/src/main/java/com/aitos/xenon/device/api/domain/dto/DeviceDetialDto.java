package com.aitos.xenon.device.api.domain.dto;

import com.aitos.xenon.core.model.BaseModel;
import lombok.Data;

@Data
public class DeviceDetialDto extends BaseModel {

    private String address;

    private Integer version;
    /**
     * Onboard入网块高
     */
    private Long startHeight;

   // private DeviceLocationDto  location;

    /**
     * 0=光伏，1=风能，2=水能
     */
    private Integer energy;

    /**
     * 设备能力
     */
    private Integer capabilities;

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
    private float sGVoltage;
    /**
     * 发电电流
     */
    private float sGCurrent;
    /**
     * 发电功率（低）
     */
    private float sGPowerLow;

    /**
     * 发电功率（高）
     */
    private float sGPowerHigh;

    /**
     * 蓄电池电压
     */
    private float sBVoltage;

    /**
     * 蓄电池电压
     */
    private float sBCurrent;

    /**
     * 蓄电池功率（低）
     */
    private float sBPowerLow;

    /**
     * 蓄电池功率（高）
     */
    private float sBPowerHigh;

    /**
     * 负载电压
     */
    private float sLVoltage;

    /**
     * 负载电压
     */
    private float sLCurrent;
    /**
     * 负载功率（低）
     */
    private float sLPowerLow;

    /**
     *负载功率（高）
     */
    private float sLPowerHigh;

    /**
     * 当日累计充电量
     */
    private float todayChargeVol;

    /**
     * 当日累计用电量
     */
    private float todayUsageVol;

    /**
     * 当日累计充电量
     */
    private float intervalChargeVol;

    /**
     * 当日累计用电量
     */
    private float intervalUsageVol;

}
