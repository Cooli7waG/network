package com.aitos.xenon.jsonrpc.service.impl;

import com.aitos.xenon.block.api.RemoteBlockService;
import com.aitos.xenon.core.constant.ApiStatus;
import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.jsonrpc.domain.vo.RpcResult;
import com.aitos.xenon.jsonrpc.service.BlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service("block")
public class BlockServiceImpl implements BlockService {

    @Autowired
    private RemoteBlockService remoteBlockService;

    @Override
    public RpcResult height() {
        Result<Long> result=new Result<>();
        result.setCode(0);
        RpcResult  rpcResult=new RpcResult();
        if(result.getCode()== ApiStatus.SUCCESS.getCode())
        {
            rpcResult.setVersion(1);
            rpcResult.setCode(result.getCode());
            rpcResult.setMessage(result.getMsg());

            Result<Long> blockHeightResult=remoteBlockService.getBlockHeight();
            HashMap<String,Long> data=new HashMap<>();
            data.put("blockHeight",blockHeightResult.getData());
            rpcResult.setResult(data);
        }else{
            rpcResult.setVersion(1);
            rpcResult.setCode(result.getCode());
            rpcResult.setMessage(result.getMsg());
        }
        return rpcResult;
    }
}
