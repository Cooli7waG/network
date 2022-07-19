package com.aitos.xenon.jsonrpc.domain.vo;

import lombok.Data;

@Data
public class RpcResultError {
    private Integer code;

    private String message;

    private String data;
}
