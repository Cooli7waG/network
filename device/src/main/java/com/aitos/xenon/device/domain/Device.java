package com.aitos.xenon.device.domain;

import com.aitos.xenon.core.model.BaseModel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Device extends BaseModel {

    private String address;

    private String name;

    /**
     * Miner类型
     */
    private Integer minerType;

    /**
     * 设备制造商名称
     */
    private String maker;
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
     * Onboard费用
     */
    private BigDecimal stakingFee;

    /**
     * 设备是否终止,0。表示未终止，1表示终止
     */
    private Integer terminate;

    private Long accountId;
}
