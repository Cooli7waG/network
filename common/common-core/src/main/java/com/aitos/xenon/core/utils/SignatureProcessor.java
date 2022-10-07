package com.aitos.xenon.core.utils;

import com.aitos.xenon.common.crypto.Base58;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.codec.Hex;
@Slf4j
public class SignatureProcessor {

    public static String signBuild(Signature  signature){
        String sign = signature.getR() + signature.getS() + Integer.toHexString(signature.getRecid()+ 27);
        log.info("sign.str={}",sign);
        return Base58.encode(Hex.decode(sign));
    }

    @Data

    public static class  Signature{
        private Integer recid;

        private String r;

        private String s;

        public Signature(String r,String s,Integer recid) {
            this.r = r;
            this.s = s;
            this.recid = recid;
        }
    }
}
