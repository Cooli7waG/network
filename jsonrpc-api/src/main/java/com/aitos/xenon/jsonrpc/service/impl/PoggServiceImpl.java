package com.aitos.xenon.jsonrpc.service.impl;

import com.aitos.xenon.block.api.RemotePoggService;
import com.aitos.xenon.block.api.domain.vo.PoggChallengeVo;
import com.aitos.xenon.core.constant.ApiStatus;
import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.jsonrpc.domain.vo.RpcResult;
import com.aitos.xenon.jsonrpc.service.PoggService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service("pogg")
public class PoggServiceImpl implements PoggService {

    @Autowired
    private RemotePoggService remotePoggService;

    @Override
    public RpcResult poggHitPerBlocks() {
        Result<HashMap<String,String>> result= remotePoggService.poggHitPerBlocks();
        RpcResult  rpcResult=new RpcResult();
        if(result.getCode()== ApiStatus.SUCCESS.getCode())
        {
            rpcResult.setVersion(1);
            rpcResult.setCode(result.getCode());
            rpcResult.setResult(result.getData());
        }else{
            rpcResult.setVersion(1);
            rpcResult.setCode(result.getCode());
            rpcResult.setMessage(result.getMsg());
        }
        return rpcResult;
    }

    @Override
    public RpcResult report(String body) {
        Result<String> result= remotePoggService.report(body);
        RpcResult  rpcResult=new RpcResult();
        if(result.getCode()== ApiStatus.SUCCESS.getCode())
        {
            String txHash=result.getData();
            rpcResult.setVersion(1);
            rpcResult.setCode(result.getCode());
            rpcResult.setTxhash(txHash);
            rpcResult.setResult(txHash);
        }else{
            rpcResult.setVersion(1);
            rpcResult.setCode(result.getCode());
            rpcResult.setMessage(result.getMsg());
        }
        return rpcResult;
    }
}
