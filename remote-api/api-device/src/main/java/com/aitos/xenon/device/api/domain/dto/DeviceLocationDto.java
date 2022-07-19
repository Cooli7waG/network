package com.aitos.xenon.device.api.domain.dto;

import com.aitos.xenon.core.model.BaseModel;
import lombok.Data;

@Data
public class DeviceLocationDto extends BaseModel {

    private Integer version;

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
}
