package com.aitos.xenon.device.api.domain.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DeviceVo  {

    private Long id;
    private String address;
    private String name;
    private String imei;
    private Long makerId;
    private String ownerAddress;
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
     * 设备是否终止,0。表示未终止，1表示终止
     */
    private Integer terminate;

    /**
     * 总挖矿收入
     */
    private BigDecimal earningMint;
    /**
     * 总服务收入
     */
    private BigDecimal earningService;

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

    /**
     * 发电功率
     */
    private Long power;
    /**
     * 平均发电功率
     */
    private Double avgPower;
    /**
     * 总发电量（千瓦时）
     */
    private Long totalEnergyGeneration;
    /**
     * 奖励收益
     */
    private String balance;

    /**
     * 总奖励
     */
    private BigDecimal totalReward;
    /**
     * 平均奖励
     */
    private BigDecimal avgReward;

    /**
     * 0.已注册，1.表示已购买，2.表示已绑定.3.表示终止
     */
    private Integer status;

    /**
     * 0：正常,1：未知,2: 异常-超两小时无数据,3: 异常-有光照但上报数据为0
     */
    private Integer runStatus;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
