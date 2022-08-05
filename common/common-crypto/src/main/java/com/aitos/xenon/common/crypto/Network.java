package com.aitos.xenon.common.crypto;

public enum Network {
    MAINNET(0x00),
    TESTNET(0x01);

    private int code;

    Network(int code) {
        this.code=code;
    }

    public int getCode() {
        return code;
    }

    public static Network findByCode(int code) {
        for (Network statusEnum : Network.values()) {
            if (statusEnum.getCode() == code) {
                return statusEnum;
            }

        }
        throw new IllegalArgumentException("code is not support");
    }
}
