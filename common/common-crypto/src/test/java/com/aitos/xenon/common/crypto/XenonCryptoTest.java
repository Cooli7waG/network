package com.aitos.xenon.common.crypto;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Test;

import javax.crypto.KeyAgreement;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class XenonCryptoTest {

    @Test
    public void test_gerateKeyPair(){
        XenonKeyPair xenonKeyPair = XenonCrypto.gerateKeyPair();
        System.out.println(xenonKeyPair);
    }
    @Test
    public void test_getAddress(){
        String address = XenonCrypto.getAddress("5ec4be5fdb4a8da8d2511f816430e5ebf6cc4d73ebdb81704d0127ae6c82782bf15f1eb86fc00c1102e33fe4ca1668801c5e4af20506575275eb899bb21f2e9c");
        System.out.println(address);
    }
    @Test
    public void test_getPublickKey(){
        String publickKey = XenonCrypto.getPublickKey("00808173ee96055fc3af64320a7ae20d611372f7240c057d1c4d89f466135efac5");
        System.out.println(publickKey);
    }

    @Test
    public void test_sign(){
        String privateKey="00808173ee96055fc3af64320a7ae20d611372f7240c057d1c4d89f466135efac5";
        String dataJson="{\"version\":1,\"minerAddress\":\"1162eqbyXo8p4JPxbLVt94e8zL8TVgyqyeF9Ytg7PhC55o\",\"ownerAddress\":\"11D1gq3XVrkp2UEgJ25afSdEZuoncyz9JngBfKZjfjQxRW\",\"payerAddress\":\"11D1gq3XVrkp2UEgJ25afSdEZuoncyz9JngBfKZjfjQxRW\",\"location\":{\"version\":1,\"locationType\":0,\"latitude\":0,\"longitude\":0},\"minerInfo\":{\"version\":1,\"startHeight\":1,\"location\":\"{\\\"version\\\":1,\\\"locationType\\\":0,\\\"latitude\\\":0,\\\"longitude\\\":0}\",\"energy\":0,\"capabilities\":1,\"totalChargeVol\":0,\"totalUsageVol\":0},\"stakingFee\":0}";

        String sign = XenonCrypto.sign(privateKey,dataJson.getBytes());
        System.out.println(sign);

        String privateKey2="00808173ee96055fc3af64320a7ae20d611372f7240c057d1c4d89f466135efac5";
        String dataJson2="{\"version\":1,\"minerAddress\":\"1162eqbyXo8p4JPxbLVt94e8zL8TVgyqyeF9Ytg7PhC55o\",\"ownerAddress\":\"11D1gq3XVrkp2UEgJ25afSdEZuoncyz9JngBfKZjfjQxRW\"}";

        String sign2 = XenonCrypto.sign(privateKey2,dataJson2.getBytes());
        System.out.println(sign2);

        String privateKey3="00808173ee96055fc3af64320a7ae20d611372f7240c057d1c4d89f466135efac5";
        String dataJson3="{\"version\":1,\"minerAddress\":\"1162eqbyXo8p4JPxbLVt94e8zL8TVgyqyeF9Ytg7PhC55o\",\"ownerAddress\":\"11D1gq3XVrkp2UEgJ25afSdEZuoncyz9JngBfKZjfjQxRW\",\"payerAddress\":\"11D1gq3XVrkp2UEgJ25afSdEZuoncyz9JngBfKZjfjQxRW\",\"location\":{\"version\":1,\"locationType\":0,\"latitude\":0,\"longitude\":0},\"minerInfo\":{\"version\":1,\"startHeight\":1,\"location\":\"{\\\"version\\\":1,\\\"locationType\\\":0,\\\"latitude\\\":0,\\\"longitude\\\":0}\",\"energy\":0,\"capabilities\":1,\"totalChargeVol\":0,\"totalUsageVol\":0},\"stakingFee\":0,\"minerSignature\":\"4CTJ9fwFDNHUqFhXRUm6Nq8aBmdZF1ErS833RExw1izjQ7FpEzT9h6KTBqWe12dt3NarxsexSmQpG9k4ue3YUC4Y\"}";

        String sign3 = XenonCrypto.sign(privateKey3,dataJson3.getBytes());
        System.out.println(sign3);
    }

    @Test
    public void test_sign2(){
        XenonKeyPair xenonKeyPair = XenonCrypto.gerateKeyPair();
        System.out.println(xenonKeyPair);

        String sign = XenonCrypto.sign(xenonKeyPair.getPrivateKey(), Hex.decode(xenonKeyPair.getPublicKey()));
        System.out.println(sign);
    }

    @Test
    public void test_verify_ecdsa(){
        String address="0xf97430b18e9318557893d25254ef681dc6ebf706";
        String data="{\"version\":1,\"minerAddress\":\"1162eqbyXo8p4JPxbLVt94e8zL8TVgyqyeF9Ytg7PhC55o\",\"ownerAddress\":\"11D1gq3XVrkp2UEgJ25afSdEZuoncyz9JngBfKZjfjQxRW\",\"payerAddress\":\"11D1gq3XVrkp2UEgJ25afSdEZuoncyz9JngBfKZjfjQxRW\",\"location\":{\"version\":1,\"locationType\":0,\"latitude\":0,\"longitude\":0},\"minerInfo\":{\"version\":1,\"startHeight\":1,\"location\":\"{\\\"version\\\":1,\\\"locationType\\\":0,\\\"latitude\\\":0,\\\"longitude\\\":0}\",\"energy\":0,\"capabilities\":1,\"totalChargeVol\":0,\"totalUsageVol\":0},\"stakingFee\":0}";
        String sign="7Br2scLxJcVozbsfHpxeQmQJ7ZjnNUTYDnZMRf8r3TDM1znTsLDbHRyUGZJdbvkQ7iBwS5Sx9jf8D5rS6JoRmPZ2r";

        boolean result = XenonCrypto.verify(address,data, sign);
        System.out.println(result);
    }

    @Test
    public void test_getPublickKey_ecdsa(){
        String data="{\"version\":1,\"ownerAddress\":\"1dMKCMoPYvdAcJPtvcrGKVXmxbNq1Vz8XBVc9yxQDcFyhmi7ixXCp9HG3fgMm4VZnG5rZvX1j7udPtRBauLypkCGWQenoophe6W6JZ2zVp2F9dDL9e1JzeoeKt4uBk83wrrETwVh2tiEztyPwFZU8zsr3Am5YFexH9TcWA9oHM8tUKEb\",\"minerAddress\":\"1PeSVZ1EixppqcbXU67KfxaqDpywS5Vi9s2SxTanqbsnFq84PAfb8jg5ZrF2zoGUprzryivguWWTATUnA8kjDKzc4a\"}";
        String signature=Base58.encode(Hex.decode("73c42a8f3ec818f6f2462eb3cf1f41b2784bacabf06584ad1c1f72ede1c1f5811e56ea577c42dfc3904178cc6362b5aa35d02cb805e997bcb3bec0ff7e6de18b1c"));

        XenonKeyPair xenonKeyPair = XenonCrypto.getPublickKey(data,signature);
        System.out.println(xenonKeyPair);

        boolean result = XenonCrypto.verify(xenonKeyPair.getAddress(),data.getBytes(), signature);
        System.out.println(result);
    }

    @Test
    public void test_verify(){
        XenonKeyPair xenonKeyPair = XenonCrypto.gerateKeyPair();

        String sign = XenonCrypto.sign(xenonKeyPair.getPrivateKey(),"test_data");

        boolean result = XenonCrypto.verify(xenonKeyPair.getPublicKey(),"test_data", sign);
        System.out.println(result);
    }

    @Test
    public void test_verify2(){
        XenonKeyPair xenonKeyPair = XenonCrypto.gerateKeyPair();

        String sign = XenonCrypto.sign(xenonKeyPair.getPrivateKey(),"test_data".getBytes());

        boolean result = XenonCrypto.verify(xenonKeyPair.getPublicKey(),"test_data".getBytes(), sign);
        System.out.println(result);
    }

    @Test
    public void test_verify3(){
        XenonKeyPair xenonKeyPair = XenonCrypto.gerateKeyPair();

        String sign = XenonCrypto.sign(xenonKeyPair.getPrivateKey(),"test_data");

        boolean result = XenonCrypto.verify(xenonKeyPair.getPublicKey(),"test_data".getBytes(), Base58.decode(sign));
        System.out.println(result);
    }

    @Test
    public void test_doECDH(){
        String privateKey="3hupFSQNWwiJuQNc68HiWzPgyNpQA2yy9iiwhytMS7rZyfCJDNrSLBqS8QguVBgam5TLWqgRFwSME86GUHpJrfGxqzgQLGB99cmU8FxzvWEg3WTGUTuCrp9XuRyJ5Sdej62WzJSVcr6Mmj9utPApB4VsqWMY4Z74v8xQx78t8wQmTR2FeBeurwAPzeJuMWB72xzA9";

        System.out.println(Hex.toHexString(Base58.decode(privateKey)));
        String publickKey="PZ8Tyr4Nx8MHsRAGMpZmZ6TWY63dXWSCyYX7kae74h2wDin6wmJwpbMqGUrKxMf2FQA3nw616bhpmXKrEEQ5A3KkcY793AsKpF7EA5Rf1Yq1scnPAXunZEQd";
        System.out.println(Hex.toHexString(Base58.decode(publickKey)));

        //publickKey="04"+Hex.toHexString(Base58.decode(publickKey));
        //publickKey=Base58.encode(Hex.decode(publickKey));

        //byte[] secret = XenonCrypto.doECDH(privateKey,publickKey);
        //System.out.println(Hex.toHexString(secret));
    }
    @Test
    public void test_doECDH2() throws Exception{
        Security.addProvider(new BouncyCastleProvider());

        KeyFactory kf = KeyFactory.getInstance("ECDH", "BC");


        ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256k1");

        KeyPairGenerator g = KeyPairGenerator.getInstance("ECDSA", "BC");

        g.initialize(ecSpec, new SecureRandom());

        KeyPair aKeyPair = g.generateKeyPair();

        System.out.println(Hex.toHexString(aKeyPair.getPrivate().getEncoded()));
        System.out.println(Hex.toHexString(aKeyPair.getPublic().getEncoded()));




        ECPrivateKeySpec prvkey = new ECPrivateKeySpec(new BigInteger(Hex.decode("257ac5637dcb8a42a9e937c0aed919508fc42e8177e2df58734aae85ea11dd5e")), ecSpec);
        PrivateKey privateKey = kf.generatePrivate(prvkey);


        ECPublicKeySpec pubKey = new ECPublicKeySpec(
                ecSpec.getCurve().decodePoint(Hex.decode("04646A6E87FE4171D3687BC3FD1C3942BEA5870C98A6C4D56247C835D4C4B0B58BF089EF589A7F34B5378919F13CC32D67BC6CEE061BD573E3C5C6045B2216D79C")), ecSpec);
        PublicKey publicKey = kf.generatePublic(pubKey);

        KeyAgreement aKeyAgree = KeyAgreement.getInstance("ECDH", "BC");
        aKeyAgree.init(aKeyPair.getPrivate());

        aKeyAgree.doPhase(aKeyPair.getPublic(), true);

        byte[] secret = aKeyAgree.generateSecret();
        System.out.println(Hex.toHexString(secret));
    }

    @Test
    public void test_doECDH3() throws Exception{
        Security.addProvider(new BouncyCastleProvider());

        ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256k1");

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", "BC");

        keyPairGenerator.initialize(ecSpec, new SecureRandom());

        KeyPair aKeyPair = keyPairGenerator.generateKeyPair();

        ECPrivateKey eckey = (ECPrivateKey)aKeyPair.getPrivate();
        byte[] prK=eckey.getD().toByteArray();

        ECPublicKey eckey2 = (ECPublicKey)aKeyPair.getPublic();
        byte[] puK=eckey2.getQ().getEncoded(true);

        System.out.println(Hex.toHexString(prK));
        System.out.println(Hex.toHexString(puK));



        PrivateKey prvk = getPrivateKey(Hex.toHexString(aKeyPair.getPrivate().getEncoded()));
        PublicKey pubk = getPublicKey(Hex.toHexString(aKeyPair.getPublic().getEncoded()));

        KeyAgreement keyAgree = KeyAgreement.getInstance("ECDH", "BC");
        keyAgree.init(prvk);
        keyAgree.doPhase(pubk, true);
        byte[] secret = keyAgree.generateSecret();
        System.out.println(Hex.toHexString(secret));
    }


    public static PrivateKey getPrivateKey(String key) throws Exception {

        byte[] bytes = Hex.decode(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 从string转publicKey
     *
     * @param key 公钥的字符串
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKey(String key) throws Exception {

        byte[] bytes = Hex.decode(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        return keyFactory.generatePublic(keySpec);
    }


    public static PrivateKey loadPrivateKey (byte [] data) throws Exception
    {
        PrivateKey key;
        ECParameterSpec params = ECNamedCurveTable.getParameterSpec("secp256k1");
        ECPrivateKeySpec prvkey = new ECPrivateKeySpec(new BigInteger(data), params);
        KeyFactory kf = KeyFactory.getInstance("ECDH", "BC");
        return kf.generatePrivate(prvkey);
    }

    public static PublicKey loadPublicKey (byte [] data) throws Exception
    {
        ECParameterSpec params = ECNamedCurveTable.getParameterSpec("secp256k1");
        ECPublicKeySpec pubKey = new ECPublicKeySpec(params.getCurve().decodePoint(data), params);
        KeyFactory kf = KeyFactory.getInstance("ECDH", "BC");
        return kf.generatePublic(pubKey);
    }
}
