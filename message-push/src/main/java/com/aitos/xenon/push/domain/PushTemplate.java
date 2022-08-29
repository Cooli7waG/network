package com.aitos.xenon.push.domain;

import com.aitos.xenon.core.model.BaseModel;
import lombok.Data;


@Data
public class PushTemplate extends BaseModel {

    private String name;

    private String content;

}
