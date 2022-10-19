package com.aitos.xenon.jsonrpc.controller;

import com.aitos.xenon.core.constant.ApiStatus;
import com.aitos.xenon.jsonrpc.common.ReflectUtils;
import com.aitos.xenon.jsonrpc.common.SpringContextUtil;
import com.aitos.xenon.jsonrpc.domain.dto.RpcRequest;
import com.aitos.xenon.jsonrpc.domain.vo.RpcResult;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping
@Slf4j
public class JsonRpcController {

    @PostMapping
    public RpcResult rpcPost(@RequestBody String body, HttpServletRequest request){
        log.info("rpcPost,ip={},body={}",request.getRemoteAddr(),body);
        RpcResult rpcResult = checkParams(body);
        if(rpcResult!=null){
            return rpcResult;
        }
        return processingData(body);
    }

    @GetMapping
    public RpcResult rpcGet(String body, HttpServletRequest request){
        log.info("rpcPost,ip={},body={}",request.getRemoteAddr(),body);
        RpcResult rpcResult = checkParams(body);
        if(rpcResult!=null){
            return rpcResult;
        }
        return processingData(body);
    }

    private RpcResult processingData(String body){
        RpcRequest rpcRequest= JSON.parseObject(body,RpcRequest.class);

        JSONObject paramsObject = JSONObject.parseObject(body, Feature.OrderedField);
        String params="";
        try{
            paramsObject=paramsObject.getJSONObject("params");
            params=paramsObject.toJSONString();
        }catch (ClassCastException e){
            JSONArray jSONArray=paramsObject.getJSONArray("params");
            params=jSONArray.toJSONString();
        }

        String[]  paths=rpcRequest.getMethod().split("/");
        Object service=SpringContextUtil.getBean(paths[1]);
        RpcResult result=ReflectUtils.invokeMethodByName(service,paths[2],params);
        return result;
    }
    public RpcResult checkParams(String body){
        if(StringUtils.hasText(body)){
            return null;
        }

        RpcResult  rpcResult=new RpcResult();
        rpcResult.setVersion(1);
        rpcResult.setCode(ApiStatus.PARAMETER_FORMATE_ERROR.getCode());
        rpcResult.setMessage(ApiStatus.PARAMETER_FORMATE_ERROR.getMsg());
        return rpcResult;
    }
}
