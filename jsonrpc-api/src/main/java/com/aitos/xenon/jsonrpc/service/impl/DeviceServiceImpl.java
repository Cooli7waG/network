package com.aitos.xenon.jsonrpc.service.impl;

import com.aitos.xenon.core.constant.ApiStatus;
import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.device.api.RemoteDeviceService;
import com.aitos.xenon.jsonrpc.domain.vo.RpcResult;
import com.aitos.xenon.jsonrpc.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service("device")
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private RemoteDeviceService remoteDeviceService;
    @Override
    public Result getHeight() {
        return null;
    }

    @Override
    public Result getBlockHeight(HashMap map) {
        return null;
    }

    @Override
    public RpcResult onboard(String params) {
        Result result=remoteDeviceService.onboard(params);
        RpcResult  rpcResult=new RpcResult();
        if(result.getCode()== ApiStatus.SUCCESS.getCode())
        {
            rpcResult.setVersion(1);
            rpcResult.setCode(result.getCode());
            rpcResult.setMessage(result.getMsg());
            rpcResult.setTxhash(result.getData().toString());
            rpcResult.setResult(result.getData().toString());
        }else{
            rpcResult.setVersion(1);
            rpcResult.setCode(result.getCode());
            rpcResult.setMessage(result.getMsg());
        }
        return rpcResult;
    }
}
