package com.aitos.xenon.push.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Data
public class PushMessage implements Serializable {
    private Long templateId;

    private String from;

    private String to;

    private String titile;

    private String content;

    private Integer messageType;

    private int badge;

    private Map<String,Object> customMap;
}