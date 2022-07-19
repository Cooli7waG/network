package com.aitos.xenon.common.crypto.ed25519;


import net.i2p.crypto.eddsa.EdDSAEngine;
import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.KeyPairGenerator;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class Ed25519 {

    public static KeyPair gerateKeyPair() {
        KeyPairGenerator edDsaKpg = new KeyPairGenerator();
        KeyPair keyPair = edDsaKpg.generateKeyPair();
        return keyPair;
    }

    public static Ed25519KeyPair gen(){
        KeyPairGenerator keyPairGenerator = new KeyPairGenerator();
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey= keyPair.getPrivate();
        PublicKey publicKey= keyPair.getPublic();

        Ed25519KeyPair ed25519KeyPair=new Ed25519KeyPair();
        ed25519KeyPair.setPrivateKey(Hex.encodeHexString(privateKey.getEncoded()));
        ed25519KeyPair.setPublicKey(Hex.encodeHexString(publicKey.getEncoded()));
        return ed25519KeyPair;
    }

    private static byte[] sign(byte[] privateKey,byte[] data){
        try {
            PKCS8EncodedKeySpec encoded = new PKCS8EncodedKeySpec(privateKey);
            EdDSAPrivateKey privateKeyTemp = new EdDSAPrivateKey(encoded);

            EdDSAEngine edDSAEngine = new EdDSAEngine();
            edDSAEngine.initSign(privateKeyTemp);
            edDSAEngine.update(data);
            byte[] signature = edDSAEngine.sign();
            return signature;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String sign(String privateKey,byte[] data)  {
        try{
            if(!privateKey.contains("302e020100300506032b657004220420")){
                privateKey="302e020100300506032b657004220420"+privateKey.toLowerCase();
            }
            byte[] signature = sign(Hex.decodeHex(privateKey),data);
            return Hex.encodeHexString(signature);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String signBase58(String privateKey,String data) {
        try{
            byte[] privateKeys=null;
            privateKey=Hex.encodeHexString(Base58.decode(privateKey));
            privateKey="302e020100300506032b657004220420"+privateKey.toLowerCase();
            privateKeys=Hex.decodeHex(privateKey);
            byte[] signature = sign(privateKeys,data.getBytes());
            return Base58.encode(signature);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static Boolean verify(byte[] publickKey,byte[] data,byte[] signature)  {
        try{
            X509EncodedKeySpec encoded = new X509EncodedKeySpec(publickKey);
            EdDSAPublicKey publicKeyTemp = new EdDSAPublicKey(encoded);

            EdDSAEngine edDSAEngine = new EdDSAEngine();
            edDSAEngine.initVerify(publicKeyTemp);
            edDSAEngine.update(data);
            Boolean verify = edDSAEngine.verify(signature);
            return verify;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static Boolean verify(String publickKey,byte[] data,byte[] signature)  {
        try{
            byte[] publickKeyBytes=Hex.decodeHex(publickKey);
            if(publickKeyBytes.length==32){
                publickKey=getPubkeyDer(publickKeyBytes);
            }

            X509EncodedKeySpec encoded = new X509EncodedKeySpec(Hex.decodeHex(publickKey));
            EdDSAPublicKey publicKeyTemp = new EdDSAPublicKey(encoded);

            EdDSAEngine edDSAEngine = new EdDSAEngine();
            edDSAEngine.initVerify(publicKeyTemp);
            edDSAEngine.update(data);
            Boolean verify = edDSAEngine.verify(signature);
            return verify;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static Boolean verifyBase58(String publickKey,String data,String signature)  {
        try{
            publickKey="302a300506032b6570032100"+Hex.encodeHexString(Base58.decode(publickKey)).toLowerCase();
            byte[] publickKeyBytes=Hex.decodeHex(publickKey);

            byte[] dataBytes=data.getBytes();

            byte[] signatureBytes=Base58.decode(signature);

            Boolean verify = verify(publickKeyBytes,dataBytes,signatureBytes);
            return verify;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static Boolean verifyBase58(String publickKey,byte[] data,String signature)  {
        try{
            publickKey="302a300506032b6570032100"+Hex.encodeHexString(Base58.decode(publickKey)).toLowerCase();
            byte[] publickKeyBytes=Hex.decodeHex(publickKey);

            byte[] dataBytes=data;

            byte[] signatureBytes=Base58.decode(signature);

            Boolean verify = verify(publickKeyBytes,dataBytes,signatureBytes);
            return verify;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static String getPubkeyDer(byte[] pubkey){
        String strReturn;
        String strpadding = "300506032b6570000330";
        String strPubkey = Hex.encodeHexString(pubkey);
        //if((pubkey[0] & 0x80) == 0x00){
            strPubkey = "00" + strPubkey;
        //}
        strPubkey = "03"+ Integer.toHexString(strPubkey.length()/2) +strPubkey;
        strReturn = strpadding+strPubkey;
        strReturn = "30" + Integer.toHexString(strReturn.length()/2)+strReturn;

        System.out.println(strReturn);
        return  strReturn;
    }
}
