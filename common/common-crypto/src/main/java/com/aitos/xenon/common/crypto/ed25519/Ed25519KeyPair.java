package com.aitos.xenon.common.crypto.ed25519;

import org.springframework.security.crypto.codec.Hex;

public class Ed25519KeyPair {
    private String privateKey;
    private String publicKey;

    public String getPrivateKey() {
        return privateKey;
    }

    public String getPrivateKeyToBase58() {
        return Base58.encode(Hex.decode(privateKey));
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getPublicKeyToBase58() {
        return Base58.encode(Hex.decode(publicKey));
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
