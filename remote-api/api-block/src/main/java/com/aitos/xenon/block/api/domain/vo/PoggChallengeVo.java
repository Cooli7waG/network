package com.aitos.xenon.block.api.domain.vo;

import lombok.Data;

@Data
public class PoggChallengeVo {

    private String random;

    private Long timeout;

    private String txHash;
}
