package com.aitos.xenon.block.domain;

import com.aitos.xenon.core.model.BaseModel;
import lombok.Data;

@Data
public class PoggReportSubtotal extends BaseModel {
    private String address;

    private String ownerAddress;

    private Integer minerType;

    private Long epoch;

    private int recordNum;
}
