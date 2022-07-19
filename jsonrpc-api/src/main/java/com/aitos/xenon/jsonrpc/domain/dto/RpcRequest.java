package com.aitos.xenon.jsonrpc.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class RpcRequest implements Serializable {

    private String jsonrpc;

    private String method;

    private Object params;

}
