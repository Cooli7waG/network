package com.aitos.xenon.block.domain;

import com.aitos.xenon.core.model.BaseModel;
import lombok.Data;

@Data
public class PoggCommit extends BaseModel {

    private String privateKey;

    private String publicKey;

    private Long height;

    private Long epoch;

    private Integer status;
}
