package com.aitos.xenon.jsonrpc.service.impl;

import com.aitos.xenon.account.api.RemoteAccountService;
import com.aitos.xenon.common.crypto.ed25519.Base58;
import com.aitos.xenon.core.constant.ApiStatus;
import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.jsonrpc.domain.vo.RpcResult;
import com.aitos.xenon.jsonrpc.service.AccountService;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service("account")
public class AccountServiceImpl implements AccountService {

    @Autowired
    private RemoteAccountService remoteAccountService;

    @Override
    public RpcResult nonce(String params) {
        JSONObject paramsJSON= JSONObject.parseObject(params);
        String address=paramsJSON.get("address").toString();
       Result<Long> result=remoteAccountService.getNonce(Hex.encodeHexString(Base58.decode(address)));
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
