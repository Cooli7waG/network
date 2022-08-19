package com.aitos.xenon.common.crypto;


import com.aitos.xenon.common.crypto.ecdsa.ECDSASignature;
import com.aitos.xenon.common.crypto.ecdsa.Hash;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.asn1.x9.X9IntegerConverter;
import org.bouncycastle.crypto.ec.CustomNamedCurves;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.math.ec.ECAlgorithms;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.custom.sec.SecP256K1Curve;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.security.SignatureException;
import java.util.Arrays;

/**
 * 功能：公钥恢复
 *
 * @Author xymoc
 * @Date 2021/10/20-14:59
 */
public class RecoverPublicKeyUtils {

    private static final Logger logger = LoggerFactory.getLogger(RecoverPublicKeyUtils.class);
    private static final X9ECParameters CURVE_PARAMS = CustomNamedCurves.getByName("secp256k1");
    public static final ECDomainParameters CURVE;
    public static final BigInteger HALF_CURVE_ORDER;

    static {
        CURVE = new ECDomainParameters(CURVE_PARAMS.getCurve(), CURVE_PARAMS.getG(), CURVE_PARAMS.getN(), CURVE_PARAMS.getH());
        HALF_CURVE_ORDER = CURVE_PARAMS.getN().shiftRight(1);
    }

    public static String recoverPublicKeyHexString(String hexString,byte[] message) throws Exception {
        return recoverPublicKey(hexString,message).toString(16);
    }

    public static BigInteger recoverPublicKey(String hexString,byte[] message) throws Exception {
        if(hexString.startsWith("0x")){
            hexString = hexString.substring(2);
        }
        byte[] bytes = HexUtils.hexStringToByteArray(hexString);
        //
        byte[] r = new byte[32];
        byte[] s = new byte[32];
        System.arraycopy(bytes,0,r,0,32);
        System.arraycopy(bytes,32,s,0,32);
        r = checkRAndS(r);
        s = checkRAndS(s);
        //
        SignatureData signatureData=new SignatureData(bytes[bytes.length-1],r ,s);
        BigInteger key = signedMessageToKey(message, signatureData);
        logger.info("恢复的公钥：{}     ,Hex String:{}",key,key.toString(16));
        return key;
    }

    private static BigInteger signedMessageToKey(byte[] message, SignatureData signatureData) throws SignatureException {
        byte[] r = signatureData.getR();
        byte[] s = signatureData.getS();
        //
        verifyPrecondition(r != null && r.length == 32, "r must be 32 bytes");
        verifyPrecondition(s != null && s.length == 32, "s must be 32 bytes");
        int header = signatureData.getV() & 255;
        if (header >= 27 && header <= 34) {
            ECDSASignature sig = new ECDSASignature(new BigInteger(1, signatureData.getR()), new BigInteger(1, signatureData.getS()));
            byte[] messageHash = Hash.sha3(message);
            int recId = header - 27;
            BigInteger key = recoverFromSignature(recId, sig, messageHash);
            if (key == null) {
                throw new SignatureException("Could not recover public key from signature");
            } else {
                return key;
            }
        } else {
            throw new SignatureException("Header byte out of range: " + header);
        }
    }

    public static BigInteger recoverFromSignature(int recId, ECDSASignature sig, byte[] message) {
        verifyPrecondition(recId >= 0, "recId must be positive");
        verifyPrecondition(sig.r.signum() >= 0, "r must be positive");
        verifyPrecondition(sig.s.signum() >= 0, "s must be positive");
        verifyPrecondition(message != null, "message cannot be null");
        BigInteger n = CURVE.getN();
        BigInteger i = BigInteger.valueOf((long)recId / 2L);
        BigInteger x = sig.r.add(i.multiply(n));
        BigInteger prime = SecP256K1Curve.q;
        if (x.compareTo(prime) >= 0) {
            return null;
        } else {
            ECPoint R = decompressKey(x, (recId & 1) == 1);
            if (!R.multiply(n).isInfinity()) {
                return null;
            } else {
                BigInteger e = new BigInteger(1, message);
                BigInteger eInv = BigInteger.ZERO.subtract(e).mod(n);
                BigInteger rInv = sig.r.modInverse(n);
                BigInteger srInv = rInv.multiply(sig.s).mod(n);
                BigInteger eInvrInv = rInv.multiply(eInv).mod(n);
                ECPoint q = ECAlgorithms.sumOfTwoMultiplies(CURVE.getG(), eInvrInv, R, srInv);
                byte[] qBytes = q.getEncoded(false);
                return new BigInteger(1, Arrays.copyOfRange(qBytes, 1, qBytes.length));
            }
        }
    }

    private static ECPoint decompressKey(BigInteger xBN, boolean yBit) {
        X9IntegerConverter x9 = new X9IntegerConverter();
        byte[] compEnc = x9.integerToBytes(xBN, 1 + x9.getByteLength(CURVE.getCurve()));
        compEnc[0] = (byte)(yBit ? 3 : 2);
        return CURVE.getCurve().decodePoint(compEnc);
    }

    public static void verifyPrecondition(boolean assertionResult, String errorMessage) {
        if (!assertionResult) {
            throw new RuntimeException(errorMessage);
        }
    }

    /**
     * 检查r或者s值的位数并做出补位
     * @param r
     * @return
     */
    public static byte[] checkRAndS(byte[] r){
        if(r.length>=32){
            return r;
        }
        String str = HexUtils.bytesToHexString(r);
        StringBuilder newStr = new StringBuilder("0x");
        String s1 = str.replaceAll("0x", "");
        int offset = 64 - s1.length();
        for (int i=0;i<offset;i++){
            newStr.append("0");
        }
        newStr.append(s1);
        logger.info("new hex str:{}",newStr.toString());
        return HexUtils.hexStringToByteArray(newStr.toString());
    }
}
