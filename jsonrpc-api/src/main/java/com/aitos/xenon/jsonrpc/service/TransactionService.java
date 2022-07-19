package com.aitos.xenon.jsonrpc.service;

import com.aitos.xenon.jsonrpc.domain.vo.RpcResult;

public interface TransactionService {
    RpcResult query(String body);

}
