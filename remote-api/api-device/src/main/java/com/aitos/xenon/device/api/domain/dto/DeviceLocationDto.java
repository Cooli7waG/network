package com.aitos.xenon.device.api.domain.dto;

import com.aitos.xenon.core.model.BaseModel;
import com.alibaba.fastjson.annotation.JSONType;
import lombok.Data;

@Data
@JSONType(orders = {"version","locationType","latitude","longitude"
        ,"h3index"})
public class DeviceLocationDto {

    private Integer version;

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
}

