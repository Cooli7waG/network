package com.aitos.xenon.common.crypto.ed25519;


import net.i2p.crypto.eddsa.EdDSAEngine;
import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.KeyPairGenerator;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveSpec;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable;
import net.i2p.crypto.eddsa.spec.EdDSAPrivateKeySpec;
import net.i2p.crypto.eddsa.spec.EdDSAPublicKeySpec;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

public class Ed25519 {

    public static Ed25519KeyPair gerateKeyPair(){
        KeyPairGenerator keyPairGenerator = new KeyPairGenerator();
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey= keyPair.getPrivate();
        PublicKey publicKey= keyPair.getPublic();

        Ed25519KeyPair ed25519KeyPair=new Ed25519KeyPair();
        byte[] privateKeyBytes=Arrays.copyOfRange(privateKey.getEncoded(),16,privateKey.getEncoded().length);
        byte[] publicKeyBytes=Arrays.copyOfRange(publicKey.getEncoded(),12,publicKey.getEncoded().length);
        ed25519KeyPair.setPrivateKey(Base58.encode(privateKeyBytes));
        ed25519KeyPair.setPublicKey(Base58.encode(publicKeyBytes));
        return ed25519KeyPair;
    }

    public static String getPublickKey(String privateKey){
        try{
            byte[] privateKeyBytes=Base58.decode(privateKey);
            EdDSANamedCurveSpec spec = EdDSANamedCurveTable.getByName(EdDSANamedCurveTable.ED_25519);
            EdDSAPrivateKey edDSAPrivateKey = new EdDSAPrivateKey(new EdDSAPrivateKeySpec(privateKeyBytes, spec));
            return  Base58.encode(edDSAPrivateKey.getAbyte());
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] sign(byte[] privateKey,byte[] data){
        try {
            EdDSANamedCurveSpec spec = EdDSANamedCurveTable.getByName(EdDSANamedCurveTable.ED_25519);
            EdDSAPrivateKey edDSAPrivateKey = new EdDSAPrivateKey(new EdDSAPrivateKeySpec(privateKey, spec));

            EdDSAEngine edDSAEngine = new EdDSAEngine();
            edDSAEngine.initSign(edDSAPrivateKey);
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
            byte[] signature = sign(Base58.decode(privateKey),data);
            return Base58.encode(signature);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String sign(String privateKey,String data) {
        try{
            String signature = sign(privateKey,data.getBytes());
            return signature;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static Boolean verify(byte[] publickKey,byte[] data,byte[] signature)  {
        try{
            EdDSANamedCurveSpec spec = EdDSANamedCurveTable.getByName(EdDSANamedCurveTable.ED_25519);
            EdDSAPublicKey publicKeyTemp = new EdDSAPublicKey(new EdDSAPublicKeySpec(publickKey,spec));

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
            byte[] publickKeyBytes=Base58.decode(publickKey);
            Boolean verify = verify(publickKeyBytes,data,signature);
            return verify;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static Boolean verify(String publickKey,String data,String signature)  {
        try{
            byte[] publickKeyBytes=Base58.decode(publickKey);

            byte[] dataBytes=data.getBytes();

            byte[] signatureBytes=Base58.decode(signature);

            Boolean verify = verify(publickKeyBytes,dataBytes,signatureBytes);
            return verify;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static Boolean verify(String publickKey,byte[] data,String signature)  {
        try{
            byte[] publickKeyBytes=Base58.decode(publickKey);

            byte[] dataBytes=data;

            byte[] signatureBytes=Base58.decode(signature);

            Boolean verify = verify(publickKeyBytes,dataBytes,signatureBytes);
            return verify;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
