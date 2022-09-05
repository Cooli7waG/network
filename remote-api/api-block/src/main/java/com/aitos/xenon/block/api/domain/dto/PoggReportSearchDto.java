package com.aitos.xenon.block.api.domain.dto;

import com.aitos.xenon.core.model.QueryParams;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class PoggReportSearchDto extends QueryParams {
    @NotEmpty(message = "address 字段不能为空")
    private String address;
    private String ownerAddress;
}
