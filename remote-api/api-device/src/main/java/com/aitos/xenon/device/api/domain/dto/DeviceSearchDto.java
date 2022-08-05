package com.aitos.xenon.device.api.domain.dto;

import com.aitos.xenon.core.model.QueryParams;
import lombok.Data;

@Data
public class DeviceSearchDto extends QueryParams {

    private String ownerAddress;

    private String address;

}
