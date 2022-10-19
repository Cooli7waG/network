package com.aitos.xenon.common.crypto;

import com.aitos.xenon.common.crypto.ecdsa.ECKeyPair;
import com.aitos.xenon.common.crypto.ecdsa.EcdsaKeyPair;
import com.aitos.xenon.common.crypto.ecdsa.Ecdsa;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Hex;

@Slf4j
public class XenonCrypto {
    /**
     * 生成公私钥
     * @return
     */
    public static XenonKeyPair gerateKeyPair(){
        XenonKeyPair  xenonKeyPair=new XenonKeyPair();
        try{
            EcdsaKeyPair keyPair = Ecdsa.getKeyPair();

            byte[] privateKeyBytes= keyPair.getPrivateKey();
            byte[] publicKeyBytes= keyPair.getPublicKey();

            String privateKey=Hex.toHexString(privateKeyBytes);
            String publicKey=Hex.toHexString(publicKeyBytes);
            xenonKeyPair.setPrivateKey(privateKey);
            xenonKeyPair.setPublicKey(publicKey);
            byte[] address=Ecdsa.getAddress(publicKeyBytes);
            xenonKeyPair.setAddress("0x"+Hex.toHexString(address));
        }catch (Exception e){
            log.error("gerateKeyPair",e);
        }
        return xenonKeyPair;
    }

    /**
     * 根据私钥获得公钥
     * @param privateKey
     * @return
     */
    public static String getPublickKey(String privateKey){
        byte[] publicKey = Ecdsa.getPublicKey(privateKey);
        return Hex.toHexString(publicKey);
    }

    /**
     * 根据原始数据和签名恢复公钥
     * @param data  原始数据
     * @param signature base58格式
     * @return
     */
    public static XenonKeyPair getPublickKey(String data,String signature){
        try{
            String signatureHex=Hex.toHexString(Base58.decode(signature));
            byte[] publicKey = Ecdsa.getPublicKey(data.getBytes(), signatureHex);

            XenonKeyPair  xenonKeyPair=new XenonKeyPair();
            xenonKeyPair.setPublicKey(Hex.toHexString(publicKey));

            byte[] address=Ecdsa.getAddress(publicKey);
            xenonKeyPair.setAddress("0x"+Hex.toHexString(address));
            return xenonKeyPair;
        }catch (Exception e){
            log.info("getEcdsaPublickKey.error:{}",e);
        }

        return null;
    }

    /**
     * 根据公钥获得地址
     * @param publicKey
     * @return
     */
    public static String getAddress(String publicKey){
        try{
            byte[] address=Ecdsa.getAddress(Hex.decode(publicKey));
            return "0x"+Hex.toHexString(address);
        }catch (Exception e){
            log.error("getAddress.error:{}",e);
        }
        return null;
    }

    /**
     * 根据私钥对数据进行签名
     * @param privateKey
     * @param data 原始数据
     * @return 返回base58格式签名
     */
    public static String sign(String privateKey,String data){
        try {
            String sign = Ecdsa.sign(privateKey,data.getBytes());
            return Base58.encode(Hex.decode(sign));
        }catch (Exception e){
            log.error("sign.error={}",e);
        }
        return null;
    }

    /**
     * 根据私钥对数据进行签名
     * @param privateKey
     * @param data 原始数据
     * @return 返回base58格式签名
     */
    public static String sign(String privateKey,byte[] data){
        try {
            String sign = Ecdsa.sign(privateKey,data);
            return Base58.encode(Hex.decode(sign));
        }catch (Exception e){
            log.error("sign.error={}",e);
        }
        return null;
    }

    /**
     * 根据公钥验证签名
     * @param address
     * @param data 原始数据
     * @param signature base58格式
     * @return boolean
     */
    public static boolean verify(String address,byte[] data,String signature){
        if (address.length() != 42) {
            throw new RuntimeException("address length error");
        }
        address=address.replace("0x","");
        byte[] signatureHex=Base58.decode(signature);
        Boolean verify = ECKeyPair.verify(address,data,signatureHex);
        return verify;
    }
    /**
     * 根据公钥验证签名
     * @param address
     * @param data 原始数据
     * @param signature base58格式
     * @return
     */
    public static boolean verify(String address,String data,String signature){
        if (address.length() != 42) {
            throw new RuntimeException("address length error");
        }
        address=address.replace("0x","");
        byte[] signatureHex=Base58.decode(signature);
        Boolean verify = ECKeyPair.verify(address,data.getBytes(),signatureHex);
        return verify;
    }
    /**
     * 根据公钥验证签名
     * @param address
     * @param data 原始数据
     * @param signature base58格式
     * @return 返回base58格式签名
     */
    public static boolean verify(String address,byte[] data,byte[] signature){
        if (address.length() != 42) {
            throw new RuntimeException("address length error");
        }
        address=address.replace("0x","");
        Boolean verify = ECKeyPair.verify(address,data,signature);
        return verify;
    }
    /*public static byte[] doECDH (String privateKeyStr, String publicKeyStr)
    {
        Security.addProvider(new BouncyCastleProvider());
        try{
            PrivateKey prvk = Ecdsa.getPrivateKey(Base58.decode(privateKeyStr));
            PublicKey pubk = Ecdsa.getPublicKey(Base58.decode(publicKeyStr));

            KeyAgreement keyAgree = KeyAgreement.getInstance("ECDH", "BC");
            keyAgree.init(prvk);
            keyAgree.doPhase(pubk, true);
            byte[] secret = keyAgree.generateSecret();
            return secret;
        }catch (Exception e){
            log.error("doECDH",e);
        }
        return null;
    }*/

}
