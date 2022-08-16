package com.aitos.xenon.block.api.domain.dto;

import com.aitos.xenon.core.model.QueryParams;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class PoggReportDto extends QueryParams {
    @NotNull(message = "请填写版本号")
    private Integer version;

    @NotEmpty(message = "address 字段不能为空")
    private String address;

    @NotEmpty(message = "dataList 字段不能为空")
    private List<String> dataList;

    @NotEmpty(message = "signature 字段不能为空")
    private String signature;

    /**
     * 下面为内部扩展字段
     */
    private String rawDataJSON;
    private List<PoggGreenDataDto> greenDataList;
    private Long epoch;
    private Integer minerType;
    private String ownerAddress;
}
