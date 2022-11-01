package com.aitos.xenon.jsonrpc.service.impl;

import com.aitos.xenon.core.constant.ApiStatus;
import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.device.api.RemoteDeviceService;
import com.aitos.xenon.jsonrpc.domain.vo.RpcResult;
import com.aitos.xenon.jsonrpc.service.DeviceService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service("device")
@Slf4j
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
        Result<String> result=remoteDeviceService.onboard(params);

        log.info("onboard.result=", JSON.toJSONString(result));
        RpcResult  rpcResult=new RpcResult();
        if(result.getCode()== ApiStatus.SUCCESS.getCode())
        {
            String txHash=result.getData();
            rpcResult.setVersion(1);
            rpcResult.setCode(result.getCode());
            rpcResult.setMessage(result.getMsg());
            rpcResult.setTxhash(txHash);
            rpcResult.setResult(txHash);
        }else{
            rpcResult.setVersion(1);
            rpcResult.setCode(result.getCode());
            rpcResult.setMessage(result.getMsg());
        }
        return rpcResult;
    }

    @Override
    public RpcResult totalEnergy(String params) {
        JSONObject jsonObject = JSON.parseObject(params);
        Result<Long> totalEnergyResult = remoteDeviceService.totalEnergy(jsonObject.getString("address"));
        Long totalEnergy = null;
        RpcResult  rpcResult=new RpcResult();
        if(totalEnergyResult.getCode()==ApiStatus.SUCCESS.getCode()){
            totalEnergy = totalEnergyResult.getData();
        }
        rpcResult.setVersion(1);
        rpcResult.setCode(totalEnergyResult.getCode());
        rpcResult.setMessage(totalEnergyResult.getMsg());
        HashMap map = new HashMap();
        map.put("totalEnergy",totalEnergy);
        rpcResult.setResult(map);
        return rpcResult;
    }


}
