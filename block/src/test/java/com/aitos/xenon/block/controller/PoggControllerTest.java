package com.aitos.xenon.block.controller;

import com.aitos.xenon.block.domain.PoggChallenge;
import com.aitos.xenon.common.crypto.ed25519.Base58;
import com.aitos.xenon.common.crypto.ed25519.Ed25519;
import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.UUID;


public class PoggControllerTest {

    @Test
    public void test_response(){
        System.out.println("miner:"+Base58.encode(Hex.decode("CB23252E6F49C497D9C9367FCF973DA4FB165B4B66F2F67B33D3AA26B3DBEB01")));

        String minerPrivateKey="302e020100300506032b65700422042079D5A2BA1630B9484090D492A0EE3A860653078B192C9643A0FDFAC576742303";
        String sign=Ed25519.sign(minerPrivateKey,Hex.decode("4D7E2521CE907E6308EE8636FC057B423FFA7918A10F6AE8E9E37560F0740DDB"));
        System.out.println("randomSignature:"+Base58.encode(Hex.decode(sign)));

        String hash="{\"random\":\"aea834ba9fa6470faaad9acb93a875a1111cee875c2d4c56808eaa1f616dccd5\",\"timeout\":139}";

        System.out.println("challenge_hash:"+Hex.toHexString(DigestUtils.sha256(hash)));


        String bodyJSON="{\"version\":1,\"random\":\"4D7E2521CE907E6308EE8636FC057B423FFA7918A10F6AE8E9E37560F0740DDB\",\"randomSignature\":\"5mSYDiUzcG1yRwu7thcesGjDhyMcp7RNT3d2nUU6yon5fXFmMK58d9tYL4xUvgG1Bakvcok1ZgACULctajA2XYdh\",\"address\":\"Efxr2W4CCNtqoE5pPHvUt6xe7weQHjKGdKNtNDXN3bdr\",\"challengeHash\":\"00112233445566778899aabbccddeeff00112233445566778899aabbccddeeff\",\"sGVoltage\":1.1,\"sGCurrent\":1.1,\"sGPowerLow\":1.1,\"sGPowerHigh\":1.1,\"sBVoltage\":1.1,\"sBCurrent\":1.1,\"sBPowerLow\":1.1,\"sBPowerHigh\":1.1,\"sLVoltage\":1.1,\"sLCurrent\":1.1,\"sLPowerLow\":1.1,\"sLPowerHigh\":1.1,\"todayChargeVol\":1.1,\"todayUsageVol\":1.1,\"intervalChargeVol\":1.1,\"intervalUsageVol\":1.1}";

        System.out.println(bodyJSON);
        String bodysign=Ed25519.sign(minerPrivateKey,bodyJSON.getBytes());
        System.out.println("signature:"+Base58.encode(Hex.decode(bodysign)));
    }


    @Test
    public void test_response2(){
       String data="DEB712471F7ACE211B8510D287035CD7758C9D7119AB458867F6CA8DA255783C147ABA48C469481C99BA89AA936BAB5650BD1E478DDB49F6BC1087C08E20AEA61EE5272831ED430771F9AC41140666BFEB632118EDBD2F3C10F36898FEFF0A38DBEE9C699705534DC63D9D8C2DA0874E5D5E5AA825BA0ABF3CEEDBC9E285850E";
        byte[] hashData=DigestUtils.sha256(Hex.decode(data));

        System.out.println(Hex.toHexString(hashData));
        byte[]  data2=new byte[hashData.length+1];
        System.arraycopy(hashData, 0, data2, 1, hashData.length);
        BigInteger bigInteger = new BigInteger(data2);

        System.out.println("bigInteger:"+bigInteger);
        System.out.println("bigInteger:"+new BigInteger(hashData));

        BigInteger result=bigInteger.remainder(new BigInteger("360"));
        System.out.println("result:"+result.compareTo(new BigInteger("-16")));
        System.out.println(result.compareTo(new BigInteger("0")));

        BigDecimal[] resBigIntegers2 = new BigDecimal(bigInteger).divideAndRemainder(new BigDecimal("360"));
        System.out.println("两数相除，整除结果为：" + resBigIntegers2[0]  +",余数为：" + resBigIntegers2[1]);


        BigInteger test=new BigInteger("-46251365817483425147254371959103685170323439972637232874520299678020965097280");
        System.out.println(Hex.toHexString(test.toByteArray()));

        BigInteger[] resBigIntegers = new BigInteger(test.toByteArray()).divideAndRemainder(new BigInteger("360"));
        System.out.println("两数相除，整除结果为：" + resBigIntegers[0]  +
                ",余数为：" + resBigIntegers[1]);





        BigInteger test2=new BigInteger("-46251365817483425147254371959103685170323439972637232874520299678020965097280");
        System.out.println(Hex.toHexString(test2.toByteArray()));

    }

    public static void test_challengeHit(){

        while (true){
            Integer timeoutRange=5;

            //随机数
            String random= UUID.randomUUID().toString().replaceAll("-","")+UUID.randomUUID().toString().replaceAll("-","");
            byte[] randomBytes=Hex.decode(random);

            // Miner私钥对随机数的签名
            String privateKey="79D5A2BA1630B9484090D492A0EE3A860653078B192C9643A0FDFAC576742303";
            String sign=Ed25519.sign(privateKey,randomBytes);
            String randomSignature=Base58.encode(org.springframework.security.crypto.codec.Hex.decode(sign));
            byte[] randomSignatureBytes=Base58.decode(randomSignature);

            //产生PoGG challenge交易hash
            long timeout=12l+timeoutRange;
            PoggChallenge pogg=new PoggChallenge();
            pogg.setRandom(random);
            pogg.setTimeout(timeout);
            String txData= JSON.toJSONString(pogg);
            System.out.println(txData);
            String txHash=DigestUtils.sha256Hex(txData);

            byte[] challengeHashBytes=Hex.decode(txHash);

            //合并数据，产生hash
            byte[]  data=new byte[challengeHashBytes.length+randomBytes.length+randomSignatureBytes.length];
            System.arraycopy(challengeHashBytes, 0, data, 0, challengeHashBytes.length);
            System.arraycopy(randomBytes, 0, data, challengeHashBytes.length, randomBytes.length);
            System.arraycopy(randomSignatureBytes, 0, data, challengeHashBytes.length+randomBytes.length, randomSignatureBytes.length);

            System.out.println("data="+Hex.toHexString(data));

            byte[] hashBytes=DigestUtils.sha256(data);

            //
            System.out.println("hashBytes="+Hex.toHexString(hashBytes));
            BigInteger bigInteger = new BigInteger(hashBytes);
            System.out.println("bigInteger="+bigInteger);
            System.out.println(Hex.toHexString(bigInteger.toByteArray()));

            BigInteger[] resBigIntegers = bigInteger.divideAndRemainder(new BigInteger("360"));
            System.out.println("两数相除，整除结果为：" + resBigIntegers[0]  +
                    ",余数为：" + resBigIntegers[1]);

            if(resBigIntegers[1].compareTo(new BigInteger("0"))==0||resBigIntegers[1].compareTo(new BigInteger("-16"))==0){
                break;
            }
        }
    }


    public static void main(String[] args) {
        test_challengeHit();
    }
}
