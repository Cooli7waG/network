package com.aitos.xenon.common.crypto;

import com.aitos.xenon.common.crypto.ecdsa.EcdsaKeyPair;
import com.aitos.xenon.common.crypto.ecdsa.Hash;
import com.aitos.xenon.common.crypto.ecdsa.Keccak256Secp256k1;
import com.aitos.xenon.common.crypto.ed25519.Base58;
import com.aitos.xenon.common.crypto.ed25519.Ed25519;
import com.aitos.xenon.common.crypto.ed25519.Ed25519KeyPair;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.KeyAgreement;
import java.security.*;
import java.util.Arrays;

@Slf4j
public class XenonCrypto {
    public static XenonKeyPair gerateKeyPair(Network network,Algorithm algorithm){
        XenonKeyPair  xenonKeyPair=new XenonKeyPair();
        if(algorithm== Algorithm.ED25519){
            xenonKeyPair.setAlgorithm(algorithm);
            xenonKeyPair.setNetwork(network);

            Ed25519KeyPair ed25519KeyPair= Ed25519.gerateKeyPair();

            xenonKeyPair.setOriginalPrivateKey(Base58.encode(ed25519KeyPair.getPrivateKey()));
            xenonKeyPair.setOriginalPublicKey(Base58.encode(ed25519KeyPair.getPublicKey()));


            byte[] oldPrivateKeyBytes= ed25519KeyPair.getPrivateKey();
            byte[] privateKeyBytes=new byte[oldPrivateKeyBytes.length+2];

            privateKeyBytes[0]=(byte)network.getCode();
            privateKeyBytes[1]=(byte)algorithm.getCode();
            System.arraycopy(oldPrivateKeyBytes, 0, privateKeyBytes, 2, oldPrivateKeyBytes.length);
            xenonKeyPair.setPrivateKey(Base58.encode(privateKeyBytes));

            byte[] oldPublicKeyBytes= ed25519KeyPair.getPublicKey();
            byte[] publicKeyBytes=new byte[oldPublicKeyBytes.length+2];

            publicKeyBytes[0]=(byte)network.getCode();
            publicKeyBytes[1]=(byte)algorithm.getCode();
            System.arraycopy(oldPublicKeyBytes, 0, publicKeyBytes, 2, oldPublicKeyBytes.length);
            xenonKeyPair.setPublicKey(Base58.encode(publicKeyBytes));
        }else if(algorithm== Algorithm.ECDSA){
            try{
                EcdsaKeyPair keyPair = Keccak256Secp256k1.getKeyPair();

                byte[] oldPrivateKeyBytes= keyPair.getPrivateKey();
                byte[] oldPublicKeyBytes= keyPair.getPublicKey();

                xenonKeyPair.setOriginalPrivateKey(Base58.encode(oldPrivateKeyBytes));
                xenonKeyPair.setOriginalPublicKey(Base58.encode(oldPublicKeyBytes));



                byte[] privateKeyBytes=new byte[oldPrivateKeyBytes.length+2];

                privateKeyBytes[0]=(byte)network.getCode();
                privateKeyBytes[1]=(byte)algorithm.getCode();
                System.arraycopy(oldPrivateKeyBytes, 0, privateKeyBytes, 2, oldPrivateKeyBytes.length);
                xenonKeyPair.setPrivateKey(Base58.encode(privateKeyBytes));

                byte[] publicKeyBytes=new byte[oldPublicKeyBytes.length+2];

                publicKeyBytes[0]=(byte)network.getCode();
                publicKeyBytes[1]=(byte)algorithm.getCode();
                System.arraycopy(oldPublicKeyBytes, 0, publicKeyBytes, 2, oldPublicKeyBytes.length);
                xenonKeyPair.setPublicKey(Base58.encode(publicKeyBytes));
            }catch (Exception e){
                log.error("gerateKeyPair",e);
            }
        }
        return xenonKeyPair;
    }

    public static String getPublickKey(String privateKey){
        XenonKeyPair xenonKeyPair=convertorPrivateKey(privateKey);
        Network network=xenonKeyPair.getNetwork();
        Algorithm algorithm=xenonKeyPair.getAlgorithm();
        if(algorithm.equals(Algorithm.ED25519)){
            byte[] originalPrivateKeyBytes=Base58.decode(xenonKeyPair.getOriginalPrivateKey());
            byte[] originalPublicKeyBytes= Ed25519.getPublickKey(originalPrivateKeyBytes);
            byte[] publicKeyBytes=new byte[originalPublicKeyBytes.length+2];
            publicKeyBytes[0]=(byte) network.getCode();
            publicKeyBytes[1]=(byte)algorithm.getCode();
            System.arraycopy(originalPublicKeyBytes, 0, publicKeyBytes, 2, originalPublicKeyBytes.length);
            return Base58.encode(publicKeyBytes);
        }else if(algorithm.equals(Algorithm.ECDSA)){
            byte[] originalPrivateKeyBytes=Base58.decode(xenonKeyPair.getOriginalPrivateKey());
            byte[] publicKey = Keccak256Secp256k1.getPublicKey(Hex.toHexString(originalPrivateKeyBytes));

            byte[] publicKeyBytes=new byte[publicKey.length+2];
            publicKeyBytes[0]=(byte) network.getCode();
            publicKeyBytes[1]=(byte)algorithm.getCode();
            System.arraycopy(publicKey, 0, publicKeyBytes, 2, publicKey.length);
            return Base58.encode(publicKeyBytes);
        }
        return null;
    }
    public static String getAddress(String publicKey){
        XenonKeyPair xenonKeyPair=convertorPublicKey(publicKey);
        Network network=xenonKeyPair.getNetwork();
        Algorithm algorithm=xenonKeyPair.getAlgorithm();
        if(algorithm.equals(Algorithm.ED25519)){
            return publicKey;
        }else if(algorithm.equals(Algorithm.ECDSA)){
            byte[] originalPublicKeyBytes=Base58.decode(xenonKeyPair.getOriginalPublicKey());
            byte[] hash = Hash.sha3(originalPublicKeyBytes);
            byte[] address=Arrays.copyOfRange(hash, hash.length - 20, hash.length);
            return Hex.toHexString(address);

            /*Keccak.Digest256 digest256 = new Keccak.Digest256();
            digest256.update(originalPublicKeyBytes,1,originalPublicKeyBytes.length-1);
            byte[] kcck = digest256.digest();
            byte[] address=Arrays.copyOfRange(kcck,kcck.length-20,kcck.length);
            return Hex.toHexString(address);*/
        }
        return null;
    }

    public static String sign(String privateKey,String data){
        try {
            XenonKeyPair xenonKeyPair = convertorPrivateKey(privateKey);
            Network network = xenonKeyPair.getNetwork();
            Algorithm algorithm = xenonKeyPair.getAlgorithm();
            if (algorithm.equals(Algorithm.ED25519)) {
                byte[] originalPrivateKeyBytes = Base58.decode(xenonKeyPair.getOriginalPrivateKey());
                byte[] signBytes = Ed25519.sign(originalPrivateKeyBytes, Base58.decode(data));
                return Base58.encode(signBytes);
            } else if (algorithm.equals(Algorithm.ECDSA)) {
                String originalPrivateKey=Hex.toHexString(Base58.decode(xenonKeyPair.getOriginalPrivateKey()));
                String sign = Keccak256Secp256k1.sign(originalPrivateKey,Base58.decode(data));
                return Base58.encode(Hex.decode(sign));
            }
        }catch (Exception e){
            log.error("sign.error={}",e);
        }
        return null;
    }

    public static String sign(String privateKey,byte[] data){
        try {
            XenonKeyPair xenonKeyPair=convertorPrivateKey(privateKey);
            Network network=xenonKeyPair.getNetwork();
            Algorithm algorithm=xenonKeyPair.getAlgorithm();
            if(algorithm.equals(Algorithm.ED25519)){
                byte[] originalPrivateKeyBytes=Base58.decode(xenonKeyPair.getOriginalPrivateKey());
                byte[] originalPublicKeyBytes= Ed25519.sign(originalPrivateKeyBytes,data);
                return Base58.encode(originalPublicKeyBytes);
            }else if(algorithm.equals(Algorithm.ECDSA)){
                String originalPrivateKey=Hex.toHexString(Base58.decode(xenonKeyPair.getOriginalPrivateKey()));
                String sign = Keccak256Secp256k1.sign(originalPrivateKey,data);
                return Base58.encode(Hex.decode(sign));
            }
        }catch (Exception e){
            log.error("sign.error={}",e);
        }
        return null;
    }

    public static boolean verify(String publicKey,byte[] data,String signature){
        XenonKeyPair xenonKeyPair=convertorPublicKey(publicKey);
        Network network=xenonKeyPair.getNetwork();
        Algorithm algorithm=xenonKeyPair.getAlgorithm();

        byte[] signatureBytes=Base58.decode(signature);
        if(algorithm.equals(Algorithm.ED25519)){
            byte[] originalPublicKeyBytes=Base58.decode(xenonKeyPair.getOriginalPublicKey());
            Boolean verify = Ed25519.verify(originalPublicKeyBytes,data,signatureBytes);
            return verify;
        }else if(algorithm.equals(Algorithm.ECDSA)){
            byte[] originalPublicKeyBytes=Base58.decode(xenonKeyPair.getOriginalPublicKey());
            String originalPublicKey=Hex.toHexString(originalPublicKeyBytes);
            Boolean verify = Keccak256Secp256k1.verify(originalPublicKey,data,signature);
            return verify;
        }
        return false;
    }

    public static boolean verify(String publicKey,String data,String signature){
        XenonKeyPair xenonKeyPair=convertorPublicKey(publicKey);
        Network network=xenonKeyPair.getNetwork();
        Algorithm algorithm=xenonKeyPair.getAlgorithm();

        byte[] dataBytes=Base58.decode(data);
        byte[] signatureBytes=Base58.decode(signature);
        if(algorithm.equals(Algorithm.ED25519)){
            byte[] originalPublicKeyBytes=Base58.decode(xenonKeyPair.getOriginalPublicKey());
            Boolean verify = Ed25519.verify(originalPublicKeyBytes,dataBytes,signatureBytes);
            return verify;
        }else if(algorithm.equals(Algorithm.ECDSA)){
            byte[] originalPublicKeyBytes=Base58.decode(xenonKeyPair.getOriginalPublicKey());
            String originalPublicKey=Hex.toHexString(originalPublicKeyBytes);
            Boolean verify = Keccak256Secp256k1.verify(originalPublicKey,dataBytes,Hex.toHexString(signatureBytes));
            return verify;
        }
        return false;
    }

    public static boolean verify(String publicKey,byte[] data,byte[] signature){
        XenonKeyPair xenonKeyPair=convertorPublicKey(publicKey);
        Network network=xenonKeyPair.getNetwork();
        Algorithm algorithm=xenonKeyPair.getAlgorithm();

        if(algorithm.equals(Algorithm.ED25519)){
            byte[] originalPublicKeyBytes=Base58.decode(xenonKeyPair.getOriginalPublicKey());
            Boolean verify = Ed25519.verify(originalPublicKeyBytes,data,signature);
            return verify;
        }else if(algorithm.equals(Algorithm.ECDSA)){
            byte[] originalPublicKeyBytes=Base58.decode(xenonKeyPair.getOriginalPublicKey());
            String originalPublicKey=Hex.toHexString(originalPublicKeyBytes);
            Boolean verify = Keccak256Secp256k1.verify(originalPublicKey,data,Hex.toHexString(signature));
            return verify;
        }
        return false;
    }



    private static XenonKeyPair convertorPrivateKey(String privateKey){
        XenonKeyPair  xenonKeyPair=new XenonKeyPair();
        byte[] privateKeyBytes= Base58.decode(privateKey);
        xenonKeyPair.setNetwork(Network.findByCode(privateKeyBytes[0]));
        xenonKeyPair.setAlgorithm(Algorithm.findByCode(privateKeyBytes[1]));
        xenonKeyPair.setPrivateKey(privateKey);

        byte[] originalPrivateKeyBytes=Arrays.copyOfRange(privateKeyBytes,2,privateKeyBytes.length);
        xenonKeyPair.setOriginalPrivateKey(Base58.encode(originalPrivateKeyBytes));
        return xenonKeyPair;
    }

    public static XenonKeyPair convertorPublicKey(String publicKey){
        XenonKeyPair  xenonKeyPair=new XenonKeyPair();
        byte[] publicKeyBytes= Base58.decode(publicKey);
        xenonKeyPair.setNetwork(Network.findByCode(publicKeyBytes[0]));
        xenonKeyPair.setAlgorithm(Algorithm.findByCode(publicKeyBytes[1]));
        xenonKeyPair.setPublicKey(publicKey);

        byte[] originalPrivateBytes=Arrays.copyOfRange(publicKeyBytes,2,publicKeyBytes.length);
        xenonKeyPair.setOriginalPublicKey(Base58.encode(originalPrivateBytes));
        return xenonKeyPair;
    }
    public static byte[] doECDH (String privateKeyStr, String publicKeyStr)
    {
        Security.addProvider(new BouncyCastleProvider());
        try{
            PrivateKey prvk = Keccak256Secp256k1.getPrivateKey(Base58.decode(privateKeyStr));
            PublicKey pubk = Keccak256Secp256k1.getPublicKey(Base58.decode(publicKeyStr));

            KeyAgreement keyAgree = KeyAgreement.getInstance("ECDH", "BC");
            keyAgree.init(prvk);
            keyAgree.doPhase(pubk, true);
            byte[] secret = keyAgree.generateSecret();
            return secret;
        }catch (Exception e){
            log.error("doECDH",e);
        }
        return null;
    }

}
