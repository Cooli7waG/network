package com.aitos.xenon.fundation.model;

import com.aitos.xenon.core.model.BaseModel;
import lombok.Data;

@Data
public class Maker extends BaseModel {

    private String maker;

    private String serviceUrl;

    private String address;

    private Integer status;
}
