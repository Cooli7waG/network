package com.aitos.xenon.device.api.domain.dto;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@JSONType(orders = {"miner","timestamp","location","power","manufacturer"
        ,"deviceType","deviceUid","signature"},
        ignores = {})
public class ReportDeviceInfoDto {
    @NotEmpty(message = "miner can not be empty")
    private String miner;

    @NotEmpty(message = " timestamp can not be empty")
    private String timestamp;

    @NotNull(message = " location can not be empty")
    private DeviceLocationDto location;

    /**
     * 设备的额定功率，单位为毫瓦
     */
    @NotEmpty(message = "power can not be empty")
    private String power;

    /**
     * 设备制造商信息
     */
    @NotEmpty(message = "manufacturer can not be empty")
    private String manufacturer;

    /**
     * 设备型号
     */
    @NotEmpty(message = "deviceType can not be empty")
    private String deviceType;

    /**
     * 设备唯一标识
     */
    @NotEmpty(message = "deviceUid can not be empty")
    private String deviceUid;


    /**
     * Miner密钥对上述信息的签名
     */
    @NotEmpty(message = "signature can not be empty")
    private String signature;

    public Long getTimestampToLong(){
        return Long.parseLong (this.timestamp, 16);
    }

    public Long getPowerToLong(){
        return Long.parseLong (this.power, 16);
    }

}
