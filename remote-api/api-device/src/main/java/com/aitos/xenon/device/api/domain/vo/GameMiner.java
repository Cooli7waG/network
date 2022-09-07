package com.aitos.xenon.device.api.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * apply和claim使用的参数
 * @author xymoc
 */
@Data
public class GameMiner {
    private String address;

    private Integer status;

    private String privateKey;

    private String foundationSignature;

    private Double latitude;

    private Double longitude;

    private LocalDateTime createTime;

}
