package com.aitos.xenon.fundation.api.domain.dto;

import lombok.Data;

/**
 * 注册minre签名
 * @author xymoc
 */
@Data
public class RegisterDto {
    /**
     * 版本信息，当前为 1
     */
    private Integer version;
    /**
     * Miner 账户地址
     */
    private String address;
    /**
     * Miner 类型
     */
    private String type;
    /**
     * 设备制造商名称
     */
    private String maker;
}
