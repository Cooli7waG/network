package com.aitos.xenon.common.crypto;

import lombok.Data;

@Data
public class XenonKeyPair {
    private String privateKey;
    private String publicKey;

    private Network  network;
    private Algorithm  algorithm;

    private String originalPrivateKey;
    private String originalPublicKey;
}
