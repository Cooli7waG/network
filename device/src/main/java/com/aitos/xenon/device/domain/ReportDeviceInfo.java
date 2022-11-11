package com.aitos.xenon.device.domain;

import com.aitos.xenon.core.model.BaseModel;
import com.aitos.xenon.device.api.domain.dto.DeviceLocationDto;
import lombok.Data;


@Data
public class ReportDeviceInfo extends BaseModel {

    private String miner;

    private Long timestamp;

    private DeviceLocationDto location;

    /**
     * 0=坐标类型位置，1=H3索引类型位置
     */
    private Integer locationType;
    /**
     *
     */
    private String latitude;

    private String longitude;

    private Long h3index;

    /**
     * 设备的额定功率，单位为毫瓦
     */
    private Long power;

    /**
     * 设备制造商信息
     */
    private String manufacturer;

    /**
     * 设备型号
     */
    private String deviceType;

    /**
     * 设备唯一标识
     */
    private String deviceUid;

}
