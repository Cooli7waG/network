package com.aitos.xenon.device.api.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class PushMessageDto implements Serializable {
    private Long templateId;

    private String from;

    private String to;

    private String titile;

    private String content;

    private Integer messageType;

    private int badge;

    private Map<String,Object> customMap;
}