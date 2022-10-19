package com.aitos.xenon.common.crypto.ed25519;

import lombok.Data;

@Data
public class Ed25519KeyPair {
    private byte[] privateKey;
    private byte[] publicKey;
}
