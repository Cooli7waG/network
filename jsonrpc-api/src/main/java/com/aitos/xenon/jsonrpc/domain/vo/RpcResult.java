package com.aitos.xenon.jsonrpc.domain.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class RpcResult implements Serializable {

    private Integer version;

    private Integer code;

    private String message;

    private String txhash;

    private Object result;
}
