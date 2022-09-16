package com.aitos.xenon.fundation.api.domain.vo;

import com.aitos.xenon.core.model.BaseModel;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MakerVo {

    private Long id;

    private String name;

    private String serviceUrl;

    private String address;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
