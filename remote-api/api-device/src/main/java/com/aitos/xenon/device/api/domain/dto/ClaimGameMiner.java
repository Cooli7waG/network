package com.aitos.xenon.device.api.domain.dto;

import lombok.Data;

/**
 * ClaimGameMiner
 * @author xymoc
 */
@Data
public class ClaimGameMiner {
    private String userAddress;
    private String minerAddress;
    private String personalSign;
}
