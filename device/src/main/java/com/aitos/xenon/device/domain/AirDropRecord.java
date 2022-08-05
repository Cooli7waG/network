package com.aitos.xenon.device.domain;

import com.aitos.xenon.core.model.BaseModel;
import lombok.Data;

@Data
public class AirDropRecord extends BaseModel {

    private String ownerAddress;

    private String minerAddress;

    private Long expiration;


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
     * 平均发电功率
     */
    private Long power;
    /**
     * 总发电量（千瓦时）
     */
    private Long totalEnergyGeneration;

    /**
     * 电力来源：
     * 0.光伏，1.风能，2.水能
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

    private Integer status;
}
