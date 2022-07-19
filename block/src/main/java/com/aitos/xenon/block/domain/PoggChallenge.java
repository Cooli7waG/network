package com.aitos.xenon.block.domain;

import com.aitos.xenon.core.model.BaseModel;
import lombok.Data;

@Data
public class PoggChallenge extends BaseModel {

    private String random;

    private Long timeout;

    private String txHash;

}
