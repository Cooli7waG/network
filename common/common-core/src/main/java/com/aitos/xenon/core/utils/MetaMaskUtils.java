package com.aitos.xenon.core.utils;

import java.nio.charset.StandardCharsets;

/**
 * 用于转换MetaMask签名原文
 * @author xymoc
 */
public class MetaMaskUtils {
    public static byte[] getMessage(String message){
        String temp = "Ethereum Signed Message:\n"+message.length()+message;
        byte[] bytes = temp.getBytes(StandardCharsets.UTF_8);
        byte[] newBytes = new byte[bytes.length+1];
        newBytes[0] = 0x19;
        System.arraycopy(bytes,0,newBytes,1,bytes.length);
        return newBytes;
    }
}
