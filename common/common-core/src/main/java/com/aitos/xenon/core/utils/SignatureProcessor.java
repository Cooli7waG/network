package com.aitos.xenon.core.utils;

import lombok.Data;

public class SignatureProcessor {

    public static String signBuild(Signature  signature){
        String sign = signature.getR() + signature.getS() + Integer.toHexString(signature.getV()+ 27);
        return sign;
    }

    @Data

    public static class  Signature{
        private Integer v;

        private String r;

        private String s;

        public Signature(String r,String s,Integer v) {
            this.r = r;
            this.s = s;
            this.v = v;
        }
    }
}
