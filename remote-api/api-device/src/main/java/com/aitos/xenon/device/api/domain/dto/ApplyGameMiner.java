package com.aitos.xenon.device.api.domain.dto;

import lombok.Data;

/**
 * 用于生成GameMiner
 * @author xymoc
 */
@Data
public class ApplyGameMiner {
    private String name;
    private String email;
    private String owner;
    private String personalSign;
}
