package com.aitos.xenon.jsonrpc.service;

import com.aitos.xenon.jsonrpc.domain.vo.RpcResult;

public interface PoggService {
    RpcResult fetch();

    RpcResult response(String body);
}
