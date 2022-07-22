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
    public RpcResult fetch() {
        Result<List<PoggChallengeVo>> result= remotePoggService.activeChallenges();
        RpcResult  rpcResult=new RpcResult();
        if(result.getCode()== ApiStatus.SUCCESS.getCode())
        {
            rpcResult.setVersion(1);
            rpcResult.setCode(result.getCode());
            List<PoggChallengeVo> list=result.getData();
            if(list.size()>0){
                rpcResult.setTxhash(list.get(result.getData().size()-1).getTxHash());
                rpcResult.setResult(list.get(result.getData().size()-1));
            }
        }else{
            rpcResult.setVersion(1);
            rpcResult.setCode(result.getCode());
            rpcResult.setMessage(result.getMsg());
        }
        return rpcResult;
    }

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
    public RpcResult response(String body) {
        Result result= remotePoggService.response(body);
        RpcResult  rpcResult=new RpcResult();
        if(result.getCode()== ApiStatus.SUCCESS.getCode())
        {
            rpcResult.setVersion(1);
            rpcResult.setCode(result.getCode());
            rpcResult.setTxhash(result.getData().toString());
            rpcResult.setResult(result.getData());
        }else{
            rpcResult.setVersion(1);
            rpcResult.setCode(result.getCode());
            rpcResult.setMessage(result.getMsg());
        }
        return rpcResult;
    }
}
