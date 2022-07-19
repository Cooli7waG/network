package com.aitos.xenon.core.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
public class BaseModel implements Serializable {

    private Long id;

    private LocalDateTime createTime;
    private Long createBy;
    private LocalDateTime updateTime;
    private Long updateBy;
    private Integer delFlag;

    private Long tenantId;

}