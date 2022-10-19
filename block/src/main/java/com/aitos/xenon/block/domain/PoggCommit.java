package com.aitos.xenon.block.domain;

import com.aitos.xenon.core.model.BaseModel;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class PoggCommit extends BaseModel {

    @JSONField(serialize = false)
    private String privateKey;

    private String publicKey;

    private Long height;

    private Long epoch;

    private Integer status;
}
