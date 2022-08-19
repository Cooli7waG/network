package com.aitos.xenon.common.crypto;

import java.util.Arrays;

/**
 * 功能：
 *
 * @Author xymoc
 * @Date 2021/10/20-15:18
 */
public class SignatureData {
    private final byte v;
    private final byte[] r;
    private final byte[] s;

    public SignatureData(byte v, byte[] r, byte[] s) {
        this.v = v;
        this.r = r;
        this.s = s;
    }

    public byte getV() {
        return this.v;
    }

    public byte[] getR() {
        return this.r;
    }

    public byte[] getS() {
        return this.s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            SignatureData that = (SignatureData)o;
            if (this.v != that.v) {
                return false;
            } else {
                return !Arrays.equals(this.r, that.r) ? false : Arrays.equals(this.s, that.s);
            }
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result = this.v;
        result = 31 * result + Arrays.hashCode(this.r);
        result = 31 * result + Arrays.hashCode(this.s);
        return result;
    }
}
