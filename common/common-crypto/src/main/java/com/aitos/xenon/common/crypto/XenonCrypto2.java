package com.aitos.xenon.common.crypto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XenonCrypto2 {
    /**
     * 生成公私钥
     * @param network
     * @param algorithm
     * @return
     *//*
    public static XenonKeyPair gerateKeyPair(Network network,Algorithm algorithm){
        XenonKeyPair  xenonKeyPair=new XenonKeyPair();
        if(algorithm== Algorithm.ED25519){
            xenonKeyPair.setAlgorithm(algorithm);
            xenonKeyPair.setNetwork(network);

            Ed25519KeyPair ed25519KeyPair= Ed25519.gerateKeyPair();

            xenonKeyPair.setOriginalPrivateKey(Base58.encode(ed25519KeyPair.getPrivateKey()));
            xenonKeyPair.setOriginalPublicKey(Base58.encode(ed25519KeyPair.getPublicKey()));


            byte[] oldPrivateKeyBytes= ed25519KeyPair.getPrivateKey();
            byte[] privateKeyBytes=new byte[oldPrivateKeyBytes.length+2];

            privateKeyBytes[0]=(byte)network.getCode();
            privateKeyBytes[1]=(byte)algorithm.getCode();
            System.arraycopy(oldPrivateKeyBytes, 0, privateKeyBytes, 2, oldPrivateKeyBytes.length);
            xenonKeyPair.setPrivateKey(Base58.encode(privateKeyBytes));

            byte[] oldPublicKeyBytes= ed25519KeyPair.getPublicKey();
            byte[] publicKeyBytes=new byte[oldPublicKeyBytes.length+2];

            publicKeyBytes[0]=(byte)network.getCode();
            publicKeyBytes[1]=(byte)algorithm.getCode();
            System.arraycopy(oldPublicKeyBytes, 0, publicKeyBytes, 2, oldPublicKeyBytes.length);
            xenonKeyPair.setPublicKey(Base58.encode(publicKeyBytes));
        }else if(algorithm== Algorithm.ECDSA){
            try{
                EcdsaKeyPair keyPair = Ecdsa.getKeyPair();

                byte[] oldPrivateKeyBytes= keyPair.getPrivateKey();
                byte[] oldPublicKeyBytes= keyPair.getPublicKey();

                xenonKeyPair.setOriginalPrivateKey(Base58.encode(oldPrivateKeyBytes));
                xenonKeyPair.setOriginalPublicKey(Base58.encode(oldPublicKeyBytes));



                byte[] privateKeyBytes=new byte[oldPrivateKeyBytes.length+2];

                privateKeyBytes[0]=(byte)network.getCode();
                privateKeyBytes[1]=(byte)algorithm.getCode();
                System.arraycopy(oldPrivateKeyBytes, 0, privateKeyBytes, 2, oldPrivateKeyBytes.length);
                xenonKeyPair.setPrivateKey(Base58.encode(privateKeyBytes));

                byte[] publicKeyBytes=new byte[oldPublicKeyBytes.length+2];

                publicKeyBytes[0]=(byte)network.getCode();
                publicKeyBytes[1]=(byte)algorithm.getCode();
                System.arraycopy(oldPublicKeyBytes, 0, publicKeyBytes, 2, oldPublicKeyBytes.length);
                xenonKeyPair.setPublicKey(Base58.encode(publicKeyBytes));
            }catch (Exception e){
                log.error("gerateKeyPair",e);
            }
        }
        return xenonKeyPair;
    }

    *//**
     * 根据私钥获得公钥
     * @param privateKey  base58格式
     * @return base58格式公钥
     *//*
    public static String getPublickKey(String privateKey){
        XenonKeyPair xenonKeyPair=convertorPrivateKey(privateKey);
        Network network=xenonKeyPair.getNetwork();
        Algorithm algorithm=xenonKeyPair.getAlgorithm();
        if(algorithm.equals(Algorithm.ED25519)){
            byte[] originalPrivateKeyBytes=Base58.decode(xenonKeyPair.getOriginalPrivateKey());
            byte[] originalPublicKeyBytes= Ed25519.getPublickKey(originalPrivateKeyBytes);
            byte[] publicKeyBytes=new byte[originalPublicKeyBytes.length+2];
            publicKeyBytes[0]=(byte) network.getCode();
            publicKeyBytes[1]=(byte)algorithm.getCode();
            System.arraycopy(originalPublicKeyBytes, 0, publicKeyBytes, 2, originalPublicKeyBytes.length);
            return Base58.encode(publicKeyBytes);
        }else if(algorithm.equals(Algorithm.ECDSA)){
            byte[] originalPrivateKeyBytes=Base58.decode(xenonKeyPair.getOriginalPrivateKey());
            byte[] publicKey = Ecdsa.getPublicKey(Hex.toHexString(originalPrivateKeyBytes));

            byte[] publicKeyBytes=new byte[publicKey.length+2];
            publicKeyBytes[0]=(byte) network.getCode();
            publicKeyBytes[1]=(byte)algorithm.getCode();
            System.arraycopy(publicKey, 0, publicKeyBytes, 2, publicKey.length);
            return Base58.encode(publicKeyBytes);
        }
        return null;
    }

    *//**
     * 根据原始数据和签名恢复公钥
     * @param network
     * @param algorithm
     * @param data  原始数据
     * @param signature base58格式
     * @return
     *//*
    public static XenonKeyPair getEcdsaPublickKey(Network network,Algorithm algorithm,String data,String signature){
        try{
            String signatureHex=Hex.toHexString(Base58.decode(signature));
            byte[] publicKey = Ecdsa.getPublicKey(data.getBytes(), signatureHex);

            XenonKeyPair  xenonKeyPair=new XenonKeyPair();
            xenonKeyPair.setNetwork(network);
            xenonKeyPair.setAlgorithm(algorithm);

            byte[] publicKeyBytes=new byte[publicKey.length+2];
            publicKeyBytes[0]=(byte) network.getCode();
            publicKeyBytes[1]=(byte)algorithm.getCode();
            System.arraycopy(publicKey, 0, publicKeyBytes, 2, publicKey.length);
            xenonKeyPair.setPublicKey(Base58.encode(publicKeyBytes));
            xenonKeyPair.setOriginalPublicKey(Base58.encode(publicKey));

            return xenonKeyPair;
        }catch (Exception e){
            log.info("getEcdsaPublickKey.error:{}",e);
        }

        return null;
    }

    *//**
     * 根据公钥获得地址
     * @param publicKey base58格式
     * @return
     *//*
    public static String getAddress(String publicKey){
        XenonKeyPair xenonKeyPair=convertorPublicKey(publicKey);
        Network network=xenonKeyPair.getNetwork();
        Algorithm algorithm=xenonKeyPair.getAlgorithm();
        if(algorithm.equals(Algorithm.ED25519)){
            return publicKey;
        }else if(algorithm.equals(Algorithm.ECDSA)){
            byte[] originalPublicKeyBytes=Base58.decode(xenonKeyPair.getOriginalPublicKey());
            byte[] hash = Hash.sha3(originalPublicKeyBytes);
            byte[] address=Arrays.copyOfRange(hash, hash.length - 20, hash.length);
            return Hex.toHexString(address);

            *//*Keccak.Digest256 digest256 = new Keccak.Digest256();
            digest256.update(originalPublicKeyBytes,1,originalPublicKeyBytes.length-1);
            byte[] kcck = digest256.digest();
            byte[] address=Arrays.copyOfRange(kcck,kcck.length-20,kcck.length);
            return Hex.toHexString(address);*//*
        }
        return null;
    }

    *//**
     * 根据私钥对数据进行签名
     * @param privateKey base58格式
     * @param data 原始数据
     * @return 返回base58格式签名
     *//*
    public static String sign(String privateKey,String data){
        try {
            XenonKeyPair xenonKeyPair = convertorPrivateKey(privateKey);
            Network network = xenonKeyPair.getNetwork();
            Algorithm algorithm = xenonKeyPair.getAlgorithm();
            if (algorithm.equals(Algorithm.ED25519)) {
                byte[] originalPrivateKeyBytes = Base58.decode(xenonKeyPair.getOriginalPrivateKey());
                byte[] signBytes = Ed25519.sign(originalPrivateKeyBytes, Base58.decode(data));
                return Base58.encode(signBytes);
            } else if (algorithm.equals(Algorithm.ECDSA)) {
                String originalPrivateKey=Hex.toHexString(Base58.decode(xenonKeyPair.getOriginalPrivateKey()));
                String sign = Ecdsa.sign(originalPrivateKey,Base58.decode(data));
                return Base58.encode(Hex.decode(sign));
            }
        }catch (Exception e){
            log.error("sign.error={}",e);
        }
        return null;
    }

    *//**
     * 根据私钥对数据进行签名
     * @param privateKey base58格式
     * @param data 原始数据
     * @return 返回base58格式签名
     *//*
    public static String sign(String privateKey,byte[] data){
        try {
            XenonKeyPair xenonKeyPair=convertorPrivateKey(privateKey);
            Network network=xenonKeyPair.getNetwork();
            Algorithm algorithm=xenonKeyPair.getAlgorithm();
            if(algorithm.equals(Algorithm.ED25519)){
                byte[] originalPrivateKeyBytes=Base58.decode(xenonKeyPair.getOriginalPrivateKey());
                byte[] originalPublicKeyBytes= Ed25519.sign(originalPrivateKeyBytes,data);
                return Base58.encode(originalPublicKeyBytes);
            }else if(algorithm.equals(Algorithm.ECDSA)){
                String originalPrivateKey=Hex.toHexString(Base58.decode(xenonKeyPair.getOriginalPrivateKey()));
                String sign = Ecdsa.sign(originalPrivateKey,data);
                return Base58.encode(Hex.decode(sign));
            }
        }catch (Exception e){
            log.error("sign.error={}",e);
        }
        return null;
    }

    *//**
     * 根据公钥验证签名
     * @param publicKey base58格式
     * @param data 原始数据
     * @param signature base58格式
     * @return 返回base58格式签名
     *//*
    public static boolean verify(String publicKey,byte[] data,String signature){
        XenonKeyPair xenonKeyPair=convertorPublicKey(publicKey);
        Network network=xenonKeyPair.getNetwork();
        Algorithm algorithm=xenonKeyPair.getAlgorithm();
        System.out.println("----------------------verify.signature:"+signature);
        byte[] signatureBytes=Base58.decode(signature);
        if(algorithm.equals(Algorithm.ED25519)){
            byte[] originalPublicKeyBytes=Base58.decode(xenonKeyPair.getOriginalPublicKey());
            Boolean verify = Ed25519.verify(originalPublicKeyBytes,data,signatureBytes);
            return verify;
        }else if(algorithm.equals(Algorithm.ECDSA)){
            byte[] originalPublicKeyBytes=Base58.decode(xenonKeyPair.getOriginalPublicKey());
            String originalPublicKey=Hex.toHexString(originalPublicKeyBytes);
            //TODO
            Boolean verify = Ecdsa.verify(originalPublicKey,data,HexUtils.bytesToHexString(signatureBytes));
            return verify;
        }
        return false;
    }

    public static boolean verifyClaim(String publicKey,byte[] data,String signature){
        XenonKeyPair xenonKeyPair=convertorPublicKey(publicKey);
        Network network=xenonKeyPair.getNetwork();
        Algorithm algorithm=xenonKeyPair.getAlgorithm();
        log.info("xenonKeyPair:{}",xenonKeyPair.getOriginalPublicKey());
        System.out.println("----------------------verify.signature:"+signature);
        if(algorithm.equals(Algorithm.ED25519)){
            byte[] signatureBytes=Base58.decode(signature);
            byte[] originalPublicKeyBytes=Base58.decode(xenonKeyPair.getOriginalPublicKey());
            Boolean verify = Ed25519.verify(originalPublicKeyBytes,data,signatureBytes);
            return verify;
        }else if(algorithm.equals(Algorithm.ECDSA)){
            byte[] originalPublicKeyBytes=Base58.decode(xenonKeyPair.getOriginalPublicKey());
            //String originalPublicKey=Hex.toHexString(originalPublicKeyBytes);
            String originalPublicKey= new String(originalPublicKeyBytes);
            System.out.println("-------------------------------------------------------");
            System.out.println("originalPublicKey："+originalPublicKey);
            System.out.println("-------------------------------------------------------");
            //TODO
            signature = signature.substring(2);
            Boolean verify = Ecdsa.verify(originalPublicKey,data,signature);
            return verify;
        }
        return false;
    }

    *//**
     * 根据公钥验证签名
     * @param publicKey base58格式
     * @param data 原始数据
     * @param signature base58格式
     * @return 返回base58格式签名
     *//*
    public static boolean verify(String publicKey,String data,String signature){
        XenonKeyPair xenonKeyPair=convertorPublicKey(publicKey);
        Network network=xenonKeyPair.getNetwork();
        Algorithm algorithm=xenonKeyPair.getAlgorithm();

        byte[] dataBytes=Base58.decode(data);
        byte[] signatureBytes=Base58.decode(signature);
        if(algorithm.equals(Algorithm.ED25519)){
            byte[] originalPublicKeyBytes=Base58.decode(xenonKeyPair.getOriginalPublicKey());
            Boolean verify = Ed25519.verify(originalPublicKeyBytes,dataBytes,signatureBytes);
            return verify;
        }else if(algorithm.equals(Algorithm.ECDSA)){
            byte[] originalPublicKeyBytes=Base58.decode(xenonKeyPair.getOriginalPublicKey());
            String originalPublicKey=Hex.toHexString(originalPublicKeyBytes);
            Boolean verify = Ecdsa.verify(originalPublicKey,dataBytes,Hex.toHexString(signatureBytes));
            return verify;
        }
        return false;
    }
    *//**
     * 根据公钥验证签名
     * @param publicKey base58格式
     * @param data 原始数据
     * @param signature base58格式
     * @return 返回base58格式签名
     *//*
    public static boolean verify(String publicKey,byte[] data,byte[] signature){
        XenonKeyPair xenonKeyPair=convertorPublicKey(publicKey);
        Network network=xenonKeyPair.getNetwork();
        Algorithm algorithm=xenonKeyPair.getAlgorithm();

        if(algorithm.equals(Algorithm.ED25519)){
            byte[] originalPublicKeyBytes=Base58.decode(xenonKeyPair.getOriginalPublicKey());
            Boolean verify = Ed25519.verify(originalPublicKeyBytes,data,signature);
            return verify;
        }else if(algorithm.equals(Algorithm.ECDSA)){
            byte[] originalPublicKeyBytes=Base58.decode(xenonKeyPair.getOriginalPublicKey());
            String originalPublicKey=Hex.toHexString(originalPublicKeyBytes);
            Boolean verify = Ecdsa.verify(originalPublicKey,data,Hex.toHexString(signature));
            return verify;
        }
        return false;
    }



    private static XenonKeyPair convertorPrivateKey(String privateKey){
        XenonKeyPair  xenonKeyPair=new XenonKeyPair();
        byte[] privateKeyBytes= Base58.decode(privateKey);
        xenonKeyPair.setNetwork(Network.findByCode(privateKeyBytes[0]));
        xenonKeyPair.setAlgorithm(Algorithm.findByCode(privateKeyBytes[1]));
        xenonKeyPair.setPrivateKey(privateKey);

        byte[] originalPrivateKeyBytes=Arrays.copyOfRange(privateKeyBytes,2,privateKeyBytes.length);
        xenonKeyPair.setOriginalPrivateKey(Base58.encode(originalPrivateKeyBytes));
        return xenonKeyPair;
    }

    public static XenonKeyPair convertorPublicKey(String publicKey){
        XenonKeyPair  xenonKeyPair=new XenonKeyPair();
        byte[] publicKeyBytes= Base58.decode(publicKey);
        xenonKeyPair.setNetwork(Network.findByCode(publicKeyBytes[0]));
        xenonKeyPair.setAlgorithm(Algorithm.findByCode(publicKeyBytes[1]));
        xenonKeyPair.setPublicKey(publicKey);

        byte[] originalPrivateBytes=Arrays.copyOfRange(publicKeyBytes,2,publicKeyBytes.length);
        xenonKeyPair.setOriginalPublicKey(Base58.encode(originalPrivateBytes));
        return xenonKeyPair;
    }


    public static byte[] doECDH (String privateKeyStr, String publicKeyStr)
    {
        Security.addProvider(new BouncyCastleProvider());
        try{
            PrivateKey prvk = Ecdsa.getPrivateKey(Base58.decode(privateKeyStr));
            PublicKey pubk = Ecdsa.getPublicKey(Base58.decode(publicKeyStr));

            KeyAgreement keyAgree = KeyAgreement.getInstance("ECDH", "BC");
            keyAgree.init(prvk);
            keyAgree.doPhase(pubk, true);
            byte[] secret = keyAgree.generateSecret();
            return secret;
        }catch (Exception e){
            log.error("doECDH",e);
        }
        return null;
    }*/

}
