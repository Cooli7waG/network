package com.aitos.xenon.common.crypto;

import com.aitos.xenon.common.crypto.ed25519.Base58;
import com.aitos.xenon.common.crypto.ed25519.Ed25519;
import com.aitos.xenon.common.crypto.ed25519.Ed25519KeyPair;
import org.junit.Test;
import org.springframework.security.crypto.codec.Hex;

public class Ed25519Test {

    @Test
    public void test_gen(){
        Ed25519KeyPair ed25519KeyPair= Ed25519.gen();
        System.out.println(ed25519KeyPair.getPrivateKey());
        System.out.println(ed25519KeyPair.getPublicKey());
        System.out.println(Base58.encode(Hex.decode(ed25519KeyPair.getPublicKey())));
    }

    @Test
    public void test_sign(){

        String privateKey="302e020100300506032b657004220420b524fe064dbc213d0c92cdba8870e0ab0ea3ce92c872fa0e9ca7cbaac3b4e357";
        String sign=Ed25519.sign(privateKey,"100000000000000000000000000000001".getBytes());
        System.out.println(Base58.encode(Hex.decode(sign)));
    }

    @Test
    public void test_verify(){
        String publickKey="CB23252E6F49C497D9C9367FCF973DA4FB165B4B66F2F67B33D3AA26B3DBEB01";
        String sign="A189CD3B83AEABD160EA37D0465062E90696011E001549149887BA44FE65E558DF9DDB5EA3FE0FCA60F8FADFE45CB66CE413169B5CAB96A4438F55BE34D0140F";
        Boolean verify=Ed25519.verify(publickKey,Hex.decode("112233445566"),Hex.decode(sign));
        System.out.println(verify);

    }


    @Test
    public void test_verify58(){
        String publickKey="DAiZeFMqonUF2rje3z6JZSxX2W4AKgb6A1fcCDBRsmDt";
        byte[] publickKeyBase58=Base58.decode(publickKey);

        byte[] randon=Hex.decode("67940fd7146640a181ccd4ab15e556df1a092b67e6c9466b8adbb3f3953bb1a1");

        String privateKey="C219AC92FAC5CD420DCBE5A7D542E152C6E11C478545CCB4E70CE83CF0974496";

        String sign=Ed25519.sign(privateKey,randon);
        System.out.println(Base58.encode(Hex.decode(sign)));

        String sign2="wCppHdPPa7uWnQu75QFSHiVWhUvoVwJeaSqS6y282Mu216hkcFm8dczxuLy9q6ZA6FsyCWFjyjYeZwfDxt4re2X";

        Boolean verify=Ed25519.verifyBase58(publickKey,randon,sign2);
        System.out.println(verify);

    }

    @Test
    public void test_base58(){
        //设备公钥转base58
        String publickKey="6ACD2F1C71AACC7672D183320F375CB59622D44742D63B63F3A38B450C11295A";
        System.out.println(Base58.encode(Hex.decode(publickKey)));

        byte[] test=Base58.decode("4HwNfwinHd1DUucChL3e73xCPPL6bFeQDRXhaT79D9TJ");
        String data= org.bouncycastle.util.encoders.Hex.toHexString(test);
        System.out.println(data);

    }

    @Test
    public void test_ArgonFoundation_sign(){
        String argonFoundationPublickKey="302a300506032b65700321004d7e10c17f92fb1cfbb2a8268c42198bfc632e973e465531ba68194a277f9247";
        String argonFoundationPrivateKey="302e020100300506032b657004220420946b840beca0d06950b64438f86cf5c24ce714321b535ea910db2dd97e828d8e";
        byte[] data=Hex.decode("CB23252E6F49C497D9C9367FCF973DA4FB165B4B66F2F67B33D3AA26B3DBEB01");
        String sign=Ed25519.sign(argonFoundationPrivateKey,data);
        System.out.println(Base58.encode(Hex.decode(sign)));
    }

    @Test
    public void test_ArgonFoundation_signbase58(){
        String argonFoundationPublickKey="302a300506032b65700321004d7e10c17f92fb1cfbb2a8268c42198bfc632e973e465531ba68194a277f9247";
        String argonFoundationPrivateKey="302e020100300506032b657004220420946b840beca0d06950b64438f86cf5c24ce714321b535ea910db2dd97e828d8e";
        String address=Base58.encode(Hex.decode("CB23252E6F49C497D9C9367FCF973DA4FB165B4B66F2F67B33D3AA26B3DBEB01"));
        System.out.println(address);
        byte[] data=Base58.decode(address);
        String sign=Ed25519.sign(argonFoundationPrivateKey,data);
        sign=Base58.encode(Hex.decode(sign));
        System.out.println(sign);
    }


    @Test
    public void test_onbord(){
        //miner
        String privateKey="1067a26273521836dbf1a0118227a610d0365617af99785f5baa83171bedb166";
        String publickKey="be0b5cb397a680f7d235e1637d6dfccb1c0de2235577afe9db32476dc385eca6";
        System.out.println("publicKey:"+Base58.encode(Hex.decode(publickKey)));
        String data="{\"version\":1,\"minerAddress\":\"GfHq2tTVk9z4eXgyN19dJf6qN5UuoQe8Kxm22TaLrTRXKXESDDCHVAxk3gbN\",\"ownerAddress\":\"GfHq2tTVk9z4eXgyHGZj8SQVRhkmERPXPUUDixb6KyAziRouLAsLJ7cWNV1V\",\"payerAddress\":\"GfHq2tTVk9z4eXgyHGZj8SQVRhkmERPXPUUDixb6KyAziRouLAsLJ7cWNV1V\",\"locationType\":0,\"latitude\":130.55555,\"longitude\":130.55555,\"h3index\":1,\"minerInfo\":{\"version\":1,\"startBlock\":1,\"latitude\":130.55555,\"longitude\":130.55555,\"h3index\":1,\"energy\":0,\"capabilities\":\"设备能力\",\"deviceModel\":\"设备型号\",\"deviceSerialNum\":\"设备序列号\",\"sGVoltage\":1,\"sGCurrent\":1,\"sGPowerLow\":1}}";
        String sign=Ed25519.sign(privateKey,data.getBytes());
        sign=Base58.encode(Hex.decode(sign));
        System.out.println("sign="+sign);

        Boolean verify=Ed25519.verify(publickKey,data.getBytes(),Base58.decode(sign));
        System.out.println(verify);

        //owner
         privateKey="7ED94019182E32C9834E7D6E967316A2F4DDE6DB9F3293E123C5C8D466BDEC8B";
         publickKey="E52C2EFA035C217B7E36EBC9E05FA572D051ACB184913086799F96415F93E8A4";
         System.out.println("publicKey:"+Base58.encode(Hex.decode(publickKey)));
         data="{\"version\":1,\"minerAddress\":\"DC938yd2TSbLqgv1jJQtCyRbUfXKhrXSQTTemiuyhz8u\",\"ownerAddress\":\"GRbSTTpUiLMDuYDAbHaFibKfgAsLVNcweoorDu3irtTV\",\"payerAddress\":\"DC938yd2TSbLqgv1jJQtCyRbUfXKhrXSQTTemiuyhz8u\",\"location\":{\"version\":1,\"locationType\":0,\"latitude\":0,\"longitude\":0},\"minerInfo\":{\"version\":1,\"startHeight\":1,\"location\":\"{\\\"version\\\":1,\\\"locationType\\\":0,\\\"latitude\\\":0,\\\"longitude\\\":0}\",\"energy\":0,\"capabilities\":1,\"totalChargeVol\":0,\"totalUsageVol\":0},\"stakingFee\":0,\"minerSignature\":\"3666Yvd9hrJkYtDV4ZRV3uUAHBY9dbhMkawHKWtwZAtp9tcqLXMudfrYtXyb9oEqvdzyDh1pmi9vP9UFkHWyVMz\"}";
        System.out.println(data);
         sign=Ed25519.sign(privateKey,data.getBytes());

         sign=Base58.encode(Hex.decode(sign));
         System.out.println("sign="+sign);

         verify=Ed25519.verify(publickKey,data.getBytes(),Base58.decode(sign));
         System.out.println(verify);

        //payer
        privateKey="7ED94019182E32C9834E7D6E967316A2F4DDE6DB9F3293E123C5C8D466BDEC8B";
        publickKey="E52C2EFA035C217B7E36EBC9E05FA572D051ACB184913086799F96415F93E8A4";
        System.out.println("publicKey:"+Base58.encode(Hex.decode(publickKey)));
        data="{\"version\":1,\"minerAddress\":\"DC938yd2TSbLqgv1jJQtCyRbUfXKhrXSQTTemiuyhz8u\",\"ownerAddress\":\"GRbSTTpUiLMDuYDAbHaFibKfgAsLVNcweoorDu3irtTV\",\"payerAddress\":\"DC938yd2TSbLqgv1jJQtCyRbUfXKhrXSQTTemiuyhz8u\",\"location\":{\"version\":1,\"locationType\":0,\"latitude\":0,\"longitude\":0},\"minerInfo\":{\"version\":1,\"startHeight\":1,\"location\":\"{\\\"version\\\":1,\\\"locationType\\\":0,\\\"latitude\\\":0,\\\"longitude\\\":0}\",\"energy\":0,\"capabilities\":1,\"totalChargeVol\":0,\"totalUsageVol\":0},\"stakingFee\":0,\"minerSignature\":\"3666Yvd9hrJkYtDV4ZRV3uUAHBY9dbhMkawHKWtwZAtp9tcqLXMudfrYtXyb9oEqvdzyDh1pmi9vP9UFkHWyVMz\",\"ownerSignature\":\"28McCoXfzxgsRfboNSsamKehKkLGT9DchFGVwCzCVbZuueR9gCsCfzS4ijUDn7TU8R4ivGMN21tKmooZy379bM1b\"}";
        sign=Ed25519.sign(privateKey,data.getBytes());
        sign=Base58.encode(Hex.decode(sign));
        System.out.println("sign="+sign);

        verify=Ed25519.verify(publickKey,data.getBytes(),Base58.decode(sign));
        System.out.println(verify);
    }


}
