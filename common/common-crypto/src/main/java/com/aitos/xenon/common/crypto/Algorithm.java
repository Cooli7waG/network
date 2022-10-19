package com.aitos.xenon.common.crypto;

import lombok.Data;

public enum Algorithm {
    ED25519(0x00),
    ECDSA(0x01);

    private int code;

    Algorithm(int code) {
        this.code=code;
    }

    public int getCode() {
        return code;
    }

    public static Algorithm findByCode(int code) {
        for (Algorithm statusEnum : Algorithm.values()) {
            if (statusEnum.getCode() == code) {
                return statusEnum;
            }

        }
        throw new IllegalArgumentException("code is not support");
    }
}
