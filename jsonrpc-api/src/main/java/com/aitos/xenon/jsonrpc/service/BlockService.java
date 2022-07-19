package com.aitos.xenon.jsonrpc.service;

import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.jsonrpc.domain.vo.RpcResult;

import java.util.HashMap;

public interface BlockService {
    RpcResult height();
}
