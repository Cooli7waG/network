package com.aitos.xenon.push.domain;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class PushMessageLog implements Serializable {

    private String id;

    private String data;

    private Integer status;

    private String errorMsg;

    private int sendCount;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
