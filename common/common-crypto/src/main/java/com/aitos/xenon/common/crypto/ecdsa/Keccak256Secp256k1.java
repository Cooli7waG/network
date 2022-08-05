package com.aitos.xenon.common.crypto.ecdsa;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class Keccak256Secp256k1 {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static void main(String[] args) throws SignatureException {
        String msg = "eyJ0cyI6MTExMTExMTExMSwidGVtcGVyYXR1cmUiOjI5OX0=";
        String privateKey="257AC5637DCB8A42A9E937C0AED919508FC42E8177E2DF58734AAE85EA11DD5E";
        String signData=sign(privateKey,msg);
        System.out.println(signData);


        String msg2 = "eyJ0cyI6MTExMTExMTExMSwidGVtcGVyYXR1cmUiOjI5OX0=";

        String sgin="20b423ff60b023d47f7688f2e88c02d859f48f60fd6199e900501c00a474dfdf01b9657516bc0a5e5195f0ee84e3fe7c63a161eafdabcca6e06d703de359a19f01";
        String publickey="04646A6E87FE4171D3687BC3FD1C3942BEA5870C98A6C4D56247C835D4C4B0B58BF089EF589A7F34B5378919F13CC32D67BC6CEE061BD573E3C5C6045B2216D79C";
        boolean verifyResult=verify(publickey,msg2,sgin);
        System.out.println(verifyResult);

        msg2 = "eyJ0cyI6MTExMTExMTE22xMSwidGVtcGVyYXR1cmUiOjI5OX0=";
         verifyResult=verify(publickey,msg2,sgin);
        System.out.println(verifyResult);
    }

    public static EcdsaKeyPair getKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {


        ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256k1");

        KeyPairGenerator g = KeyPairGenerator.getInstance("ECDSA", "BC");

        g.initialize(ecSpec, new SecureRandom());

        KeyPair keyPair = g.generateKeyPair();

        EcdsaKeyPair  ecdsaKeyPair=new EcdsaKeyPair();
        ecdsaKeyPair.setPrivateKey(keyPair.getPrivate().getEncoded());
        ecdsaKeyPair.setPublicKey(keyPair.getPublic().getEncoded());
        return ecdsaKeyPair;
    }

    public static PrivateKey getPrivateKey(byte[] key) throws Exception {

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        return keyFactory.generatePrivate(keySpec);
    }

    public static PublicKey getPublicKey(byte[] key) throws Exception {

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        return keyFactory.generatePublic(keySpec);
    }


    public static String sign(String privateKey,String data){

        BigInteger privKey = new BigInteger(privateKey, 16);
        BigInteger pubKey = Sign.publicKeyFromPrivate(privKey);
        ECKeyPair keyPair = new ECKeyPair(privKey, pubKey);
        System.out.println("Private key: " + privKey.toString(16));
        System.out.println("Public key: " + pubKey.toString(16));

        byte[] msgHash = Hash.sha3(data.getBytes());
        Sign.SignatureData signature = Sign.signMessage(msgHash, keyPair, false);

        String sgin= Hex.toHexString(signature.getR())+ Hex.toHexString(signature.getS())+ Hex.toHexString(signature.getV());

        return sgin;
    }


    public static boolean verify(String publicKey,String data,String signData){
        if(!publicKey.startsWith("04")){
            publicKey="04"+publicKey;
        }

        byte[] pubKey = Hex.decode(publicKey);
        BigInteger r = new BigInteger(signData.substring(0, 64),16);
        BigInteger s = new BigInteger(signData.substring(64, 128),16);

        ECDSASignature signature2=new ECDSASignature(r,s);

        byte[] sha3Str = Hash.sha3(data.getBytes());
        boolean result=ECKeyPair.verify(sha3Str,signature2,pubKey);
        return result;
    }
}
