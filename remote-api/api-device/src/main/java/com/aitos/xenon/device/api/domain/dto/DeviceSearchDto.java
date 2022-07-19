package com.aitos.xenon.device.api.domain.dto;

import com.aitos.xenon.core.model.QueryParams;
import lombok.Data;

@Data
public class DeviceSearchDto extends QueryParams {

    private Long accountId;

    private String address;

}
