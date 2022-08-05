package com.aitos.xenon.device.api.domain.dto;

import com.aitos.xenon.core.model.BaseModel;
import lombok.Data;

@Data
public class DeviceInfoDto extends BaseModel {

    private String address;

    private Integer version;
    /**
     * 0=光伏，1=风能，2=水能
     */
    private Integer energy;

    /**
     * 设备能力
     */
    private String capabilities;
    /**
     * 装机容量
     */
    private Long power;
    /**
     * 设备型号
     */
    private String deviceModel;

    /**
     * 设备序列号
     */
    private String deviceSerialNum;

}
