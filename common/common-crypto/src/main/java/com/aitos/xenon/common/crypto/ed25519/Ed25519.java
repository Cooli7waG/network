package com.aitos.xenon.common.crypto.ed25519;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.Signer;
import org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator;
import org.bouncycastle.crypto.params.Ed25519KeyGenerationParameters;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.crypto.signers.Ed25519Signer;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.nio.charset.StandardCharsets;
import java.security.*;

public class Ed25519 {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static Ed25519KeyPair gerateKeyPair(){
        Ed25519KeyPairGenerator keyPairGenerator = new Ed25519KeyPairGenerator();
        keyPairGenerator.init(new Ed25519KeyGenerationParameters(new SecureRandom()));
        AsymmetricCipherKeyPair asymmetricCipherKeyPair = keyPairGenerator.generateKeyPair();
        Ed25519PrivateKeyParameters privateKey = (Ed25519PrivateKeyParameters) asymmetricCipherKeyPair.getPrivate();
        Ed25519PublicKeyParameters publicKey = (Ed25519PublicKeyParameters) asymmetricCipherKeyPair.getPublic();

        Ed25519KeyPair ed25519KeyPair=new Ed25519KeyPair();
        ed25519KeyPair.setPrivateKey(privateKey.getEncoded());
        ed25519KeyPair.setPublicKey(publicKey.getEncoded());
        return ed25519KeyPair;
    }

    public static byte[] getPublickKey(byte[] privateKey){
            Ed25519PrivateKeyParameters privateKeyRebuild = new Ed25519PrivateKeyParameters(privateKey, 0);
            Ed25519PublicKeyParameters publicKeyRebuild = privateKeyRebuild.generatePublicKey();
            return  publicKeyRebuild.getEncoded();
    }

    public static byte[] sign(byte[] privateKey,byte[] data) throws CryptoException {
            Ed25519PrivateKeyParameters privateKeyRebuild = new Ed25519PrivateKeyParameters(privateKey, 0);

            Signer signer = new Ed25519Signer();
            signer.init(true, privateKeyRebuild);
            signer.update(data, 0, data.length);
            byte[] signature = signer.generateSignature();
            return signature;
    }

    public static Boolean verify(byte[] publickKey,byte[] data,byte[] signature)  {
            Ed25519PublicKeyParameters publicKeyRebuild = new Ed25519PublicKeyParameters(publickKey, 0);
            Signer verifierDerived = new Ed25519Signer();
            verifierDerived.init(false, publicKeyRebuild);
            verifierDerived.update(data, 0, data.length);
            boolean result = verifierDerived.verifySignature(signature);
            return result;
    }
}