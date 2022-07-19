package com.aitos.xenon.block.domain;

import com.aitos.xenon.core.model.BaseModel;
import lombok.Data;

@Data
public class PoggChallengeRecord extends BaseModel {

    private String random;

    private Long deviceId;

    private String address;

    private Integer status;

    private String data;

}
