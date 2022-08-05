package com.aitos.xenon.common.crypto;

import com.aitos.xenon.common.crypto.ed25519.Base58;
import com.aitos.xenon.common.crypto.ed25519.Ed25519KeyPair;
import com.aitos.xenon.common.crypto.ed25519.Ed25519;
import org.bouncycastle.crypto.CryptoException;
import org.junit.Test;
import org.springframework.security.crypto.codec.Hex;

public class Ed25519Test {

    @Test
    public void test_gen(){
        Ed25519KeyPair ed25519KeyPair= Ed25519.gerateKeyPair();
        System.out.println(Hex.encode(ed25519KeyPair.getPrivateKey()));
        System.out.println(Hex.encode(ed25519KeyPair.getPublicKey()));
    }

    @Test
    public void test_getPublickKey(){
        byte[] publickKey= Ed25519.getPublickKey(Base58.decode("H5EUV4jrUY1ddSjrfvpCXSYTa5PT7FB9WGt896g5nrXx"));
        System.out.println(Hex.encode(publickKey));
    }

    @Test
    public void test_sign() throws CryptoException {

        String privateKey="H5EUV4jrUY1ddSjrfvpCXSYTa5PT7FB9WGt896g5nrXx";
        byte[] sign= Ed25519.sign(Base58.decode(privateKey),Base58.decode("6DVtNLWnbiyJ7NWt2wLR5PCS84h36UK68eBw9dUbhGbx"));
        System.out.println(Base58.encode(sign));
    }

    @Test
    public void test_verify(){
        String publickKey="8kh5fpTiTRf54wTgbdq88um8pjj3Pd3kJS6XC2P9EfBM";
        String sign="2zX8BDuGWJV47yH2gPaN2paP2ZYSW4pv1DcMJARH6dDZmvtjcte8YiT2pAK6avf5NvZHDKPLp2dcEidPLMKvF37X";
        Boolean verify= Ed25519.verify(Base58.decode(publickKey),Base58.decode("6DVtNLWnbiyJ7NWt2wLR5PCS84h36UK68eBw9dUbhGbx"),Base58.decode(sign));
        System.out.println(verify);

    }


    @Test
    public void test_verify2(){
        String publickKey="DAiZeFMqonUF2rje3z6JZSxX2W4AKgb6A1fcCDBRsmDt";

        byte[] randon=Hex.decode("67940fd7146640a181ccd4ab15e556df1a092b67e6c9466b8adbb3f3953bb1a1");

        String sign2="wCppHdPPa7uWnQu75QFSHiVWhUvoVwJeaSqS6y282Mu216hkcFm8dczxuLy9q6ZA6FsyCWFjyjYeZwfDxt4re2X";

        Boolean verify= Ed25519.verify(Base58.decode(publickKey),randon,Base58.decode(sign2));
        System.out.println(verify);

    }


    @Test
    public void test_onbord() throws CryptoException {
        //miner
        String privateKey="1067a26273521836dbf1a0118227a610d0365617af99785f5baa83171bedb166";
        String publickKey="be0b5cb397a680f7d235e1637d6dfccb1c0de2235577afe9db32476dc385eca6";
        System.out.println("publicKey:"+Base58.encode(Hex.decode(publickKey)));
        String data="{\"version\":1,\"minerAddress\":\"GfHq2tTVk9z4eXgyN19dJf6qN5UuoQe8Kxm22TaLrTRXKXESDDCHVAxk3gbN\",\"ownerAddress\":\"GfHq2tTVk9z4eXgyHGZj8SQVRhkmERPXPUUDixb6KyAziRouLAsLJ7cWNV1V\",\"payerAddress\":\"GfHq2tTVk9z4eXgyHGZj8SQVRhkmERPXPUUDixb6KyAziRouLAsLJ7cWNV1V\",\"locationType\":0,\"latitude\":130.55555,\"longitude\":130.55555,\"h3index\":1,\"minerInfo\":{\"version\":1,\"startBlock\":1,\"latitude\":130.55555,\"longitude\":130.55555,\"h3index\":1,\"energy\":0,\"capabilities\":\"设备能力\",\"deviceModel\":\"设备型号\",\"deviceSerialNum\":\"设备序列号\",\"sGVoltage\":1,\"sGCurrent\":1,\"sGPowerLow\":1}}";
        String sign= Base58.encode(Ed25519.sign(Base58.decode(privateKey),data.getBytes()));
        sign=Base58.encode(Hex.decode(sign));
        System.out.println("sign="+sign);

        Boolean verify= Ed25519.verify(Base58.decode(publickKey),data.getBytes(),Base58.decode(sign));
        System.out.println(verify);

        //owner
         privateKey="7ED94019182E32C9834E7D6E967316A2F4DDE6DB9F3293E123C5C8D466BDEC8B";
         publickKey="E52C2EFA035C217B7E36EBC9E05FA572D051ACB184913086799F96415F93E8A4";
         System.out.println("publicKey:"+Base58.encode(Hex.decode(publickKey)));
         data="{\"version\":1,\"minerAddress\":\"DC938yd2TSbLqgv1jJQtCyRbUfXKhrXSQTTemiuyhz8u\",\"ownerAddress\":\"GRbSTTpUiLMDuYDAbHaFibKfgAsLVNcweoorDu3irtTV\",\"payerAddress\":\"DC938yd2TSbLqgv1jJQtCyRbUfXKhrXSQTTemiuyhz8u\",\"location\":{\"version\":1,\"locationType\":0,\"latitude\":0,\"longitude\":0},\"minerInfo\":{\"version\":1,\"startHeight\":1,\"location\":\"{\\\"version\\\":1,\\\"locationType\\\":0,\\\"latitude\\\":0,\\\"longitude\\\":0}\",\"energy\":0,\"capabilities\":1,\"totalChargeVol\":0,\"totalUsageVol\":0},\"stakingFee\":0,\"minerSignature\":\"3666Yvd9hrJkYtDV4ZRV3uUAHBY9dbhMkawHKWtwZAtp9tcqLXMudfrYtXyb9oEqvdzyDh1pmi9vP9UFkHWyVMz\"}";
        System.out.println(data);
         sign= Base58.encode(Ed25519.sign(Base58.decode(privateKey),data.getBytes()));

         sign=Base58.encode(Hex.decode(sign));
         System.out.println("sign="+sign);

         verify= Ed25519.verify(Base58.decode(publickKey),data.getBytes(),Base58.decode(sign));
         System.out.println(verify);

        //payer
        privateKey="7ED94019182E32C9834E7D6E967316A2F4DDE6DB9F3293E123C5C8D466BDEC8B";
        publickKey="E52C2EFA035C217B7E36EBC9E05FA572D051ACB184913086799F96415F93E8A4";
        System.out.println("publicKey:"+Base58.encode(Hex.decode(publickKey)));
        data="{\"version\":1,\"minerAddress\":\"DC938yd2TSbLqgv1jJQtCyRbUfXKhrXSQTTemiuyhz8u\",\"ownerAddress\":\"GRbSTTpUiLMDuYDAbHaFibKfgAsLVNcweoorDu3irtTV\",\"payerAddress\":\"DC938yd2TSbLqgv1jJQtCyRbUfXKhrXSQTTemiuyhz8u\",\"location\":{\"version\":1,\"locationType\":0,\"latitude\":0,\"longitude\":0},\"minerInfo\":{\"version\":1,\"startHeight\":1,\"location\":\"{\\\"version\\\":1,\\\"locationType\\\":0,\\\"latitude\\\":0,\\\"longitude\\\":0}\",\"energy\":0,\"capabilities\":1,\"totalChargeVol\":0,\"totalUsageVol\":0},\"stakingFee\":0,\"minerSignature\":\"3666Yvd9hrJkYtDV4ZRV3uUAHBY9dbhMkawHKWtwZAtp9tcqLXMudfrYtXyb9oEqvdzyDh1pmi9vP9UFkHWyVMz\",\"ownerSignature\":\"28McCoXfzxgsRfboNSsamKehKkLGT9DchFGVwCzCVbZuueR9gCsCfzS4ijUDn7TU8R4ivGMN21tKmooZy379bM1b\"}";
        sign= Base58.encode(Ed25519.sign(Base58.decode(privateKey),data.getBytes()));
        sign=Base58.encode(Hex.decode(sign));
        System.out.println("sign="+sign);

        verify= Ed25519.verify(Base58.decode(publickKey),data.getBytes(),Base58.decode(sign));
        System.out.println(verify);
    }


}
