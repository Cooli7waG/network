package com.aitos.xenon.device.service;

import com.aitos.xenon.common.crypto.ed25519.Base58;
import com.aitos.xenon.common.crypto.ed25519.Ed25519;
import com.aitos.xenon.core.constant.BusinessConstants;
import com.aitos.xenon.device.DeviceApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.codec.Hex;

@SpringBootTest
public class ArgonFoundation {

    @Value("${foundation.privateKey}")
    private String foundationPrivateKey;

    @Value("${foundation.publicKey}")
    private String foundationPublicKey;

    @Test
    public void test_sign(){
        String minerAddress="73331A39E6A03D49724EBA76FECC062FA9293464F149A933813DB8D6F70D03B8";
        System.out.println("minerAddress="+Base58.encode(Hex.decode(minerAddress)));
        String sign= Ed25519.sign(foundationPrivateKey, Hex.decode(minerAddress));

        String signstr= Base58.encode(Hex.decode(sign));
        System.out.println("sign="+signstr);

        Boolean verify=Ed25519.verify(foundationPublicKey,
                Hex.decode(minerAddress),
                Base58.decode(signstr));
        System.out.println("verify="+verify);
    }


    @Test
    public void test_onboard(){
        String minerAddress="B526B82511B61DC66BC514F53335316523A649747B088A4F4BD19430D18AAC4E";
        String minerPrivateKey="76B7A460BF48AE0B1E2E909B6A0DE27E7BC6AD0F8DA8C008A6E4FDA8EC69F74F";

        String ownerAddress="E52C2EFA035C217B7E36EBC9E05FA572D051ACB184913086799F96415F93E8A4";
        String ownerPrivateKey="7ED94019182E32C9834E7D6E967316A2F4DDE6DB9F3293E123C5C8D466BDEC8B";

        String payerAddress="E52C2EFA035C217B7E36EBC9E05FA572D051ACB184913086799F96415F93E8A4";
        String payerPrivateKey="7ED94019182E32C9834E7D6E967316A2F4DDE6DB9F3293E123C5C8D466BDEC8B";

        String data="{\"version\":1,\"minerAddress\":\""+Base58.encode(Hex.decode(minerAddress))+"\",\"ownerAddress\":\""+Base58.encode(Hex.decode(ownerAddress))+"\",\"payerAddress\":\""+Base58.encode(Hex.decode(payerAddress))+"\",\"location\":{\"version\":1,\"locationType\":0,\"latitude\":130.55555,\"longitude\":130.55555,\"h3index\":1},\"minerInfo\":{\"version\":1,\"startBlock\":1,\"latitude\":130.55555,\"longitude\":130.55555,\"h3index\":1,\"energy\":0,\"capabilities\":1,\"deviceModel\":\"设备型号\",\"deviceSerialNum\":\"设备序列号\",\"sGVoltage\":1,\"sGCurrent\":1,\"sGPowerLow\":1}}";
        String sign= Ed25519.sign(minerPrivateKey, data.getBytes());
        String signstr= Base58.encode(Hex.decode(sign));


        Boolean verify=Ed25519.verify(minerAddress,
                data.getBytes(),
                Base58.decode(signstr));
        System.out.println("verify="+verify);



        String minerSignData="{\"version\":1,\"minerAddress\":\""+Base58.encode(Hex.decode(minerAddress))+"\",\"ownerAddress\":\""+Base58.encode(Hex.decode(ownerAddress))+"\",\"payerAddress\":\""+Base58.encode(Hex.decode(payerAddress))+"\",\"location\":{\"version\":1,\"locationType\":0,\"latitude\":130.55555,\"longitude\":130.55555,\"h3index\":1},\"minerInfo\":{\"version\":1,\"startBlock\":1,\"latitude\":130.55555,\"longitude\":130.55555,\"h3index\":1,\"energy\":0,\"capabilities\":1,\"deviceModel\":\"设备型号\",\"deviceSerialNum\":\"设备序列号\",\"sGVoltage\":1,\"sGCurrent\":1,\"sGPowerLow\":1},\"minerSignature\":\""+signstr+"\"}";

        System.out.println("minerSignData="+minerSignData);


    }


    @Test
    public void test_onboard2(){
        String minerAddress="B526B82511B61DC66BC514F53335316523A649747B088A4F4BD19430D18AAC4E";
        String minerPrivateKey="76B7A460BF48AE0B1E2E909B6A0DE27E7BC6AD0F8DA8C008A6E4FDA8EC69F74F";

        String ownerAddress="E52C2EFA035C217B7E36EBC9E05FA572D051ACB184913086799F96415F93E8A4";
        String ownerPrivateKey="7ED94019182E32C9834E7D6E967316A2F4DDE6DB9F3293E123C5C8D466BDEC8B";

        String payerAddress="5942835be35b236df29330318398d1e3ad3352d7f764cb7f841bf1b24f52c832";
        String payerPrivateKey="41bf93225e2ec0d96610a17c2428ef6507d26ab91096ee7113ee9eb1e1b66661";

        String data="{\"version\":1,\"minerAddress\":\"DC938yd2TSbLqgv1jJQtCyRbUfXKhrXSQTTemiuyhz8u\",\"ownerAddress\":\"GRbSTTpUiLMDuYDAbHaFibKfgAsLVNcweoorDu3irtTV\",\"payerAddress\":\"DC938yd2TSbLqgv1jJQtCyRbUfXKhrXSQTTemiuyhz8u\",\"location\":{\"version\":1,\"locationType\":0,\"latitude\":0,\"longitude\":0},\"minerInfo\":{\"version\":1,\"startHeight\":1,\"location\":\"{\\\"version\\\":1,\\\"locationType\\\":0,\\\"latitude\\\":0,\\\"longitude\\\":0}\",\"energy\":0,\"capabilities\":1,\"totalChargeVol\":0,\"totalUsageVol\":0},\"stakingFee\":0,\"minerSignature\":\"3666Yvd9hrJkYtDV4ZRV3uUAHBY9dbhMkawHKWtwZAtp9tcqLXMudfrYtXyb9oEqvdzyDh1pmi9vP9UFkHWyVMz\"}";
        String sign= Ed25519.sign(ownerPrivateKey, data.getBytes());
        String signstr= Base58.encode(Hex.decode(sign));
        System.out.println(signstr);


        Boolean verify=Ed25519.verify(ownerAddress,
                data.getBytes(),
                Base58.decode(signstr));
        System.out.println("verify="+verify);

    }
}
