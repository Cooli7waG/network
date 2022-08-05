package com.aitos.xenon.common.crypto.ecdsa;

import lombok.Data;

@Data
public class EcdsaKeyPair {

    private byte[] privateKey;

    private byte[] publicKey;
}
