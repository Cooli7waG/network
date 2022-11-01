package com.aitos.xenon.jsonrpc.service;

import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.jsonrpc.domain.vo.RpcResult;

import java.util.HashMap;

public interface DeviceService {
    public Result getHeight();

    Result getBlockHeight(HashMap map);

    RpcResult onboard(String params);

    RpcResult totalEnergy(String params);
}
