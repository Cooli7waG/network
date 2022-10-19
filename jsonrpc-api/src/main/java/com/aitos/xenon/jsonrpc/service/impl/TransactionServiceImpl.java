package com.aitos.xenon.jsonrpc.service.impl;

import com.aitos.xenon.account.api.RemoteTransactionService;
import com.aitos.xenon.account.api.domain.vo.TransactionVo;
import com.aitos.xenon.core.constant.ApiStatus;
import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.jsonrpc.domain.vo.RpcResult;
import com.aitos.xenon.jsonrpc.service.TransactionService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.attribute.HashAttributeSet;
import java.util.HashMap;

@Service("transaction")
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private RemoteTransactionService remoteTransactionService;
    @Override
    public RpcResult query(String body) {
        HashMap<String,String> params= JSON.parseObject(body,HashMap.class);
        Result<TransactionVo> result=remoteTransactionService.query(params.get("txHash"));
        RpcResult  rpcResult=new RpcResult();
        if(result.getCode()== ApiStatus.SUCCESS.getCode())
        {
            rpcResult.setVersion(1);
            rpcResult.setCode(result.getCode());
            rpcResult.setMessage(result.getMsg());
            rpcResult.setResult(result.getData());
        }else{
            rpcResult.setVersion(1);
            rpcResult.setCode(result.getCode());
            rpcResult.setMessage(result.getMsg());
        }
        return rpcResult;
    }
}
