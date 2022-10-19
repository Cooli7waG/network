package com.aitos.blockchain.web3j;

import org.bouncycastle.util.encoders.Hex;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

import java.util.Arrays;

public class We3jUtils {

    public static Web3JKeyPair  genKeyPair(){
        Web3JKeyPair web3JKeyPair=new Web3JKeyPair();
        try{
            ECKeyPair ecKeyPair = Keys.createEcKeyPair();

            web3JKeyPair.setPrivateKey(ecKeyPair.getPrivateKey().toString(16));
            web3JKeyPair.setPublicKey(ecKeyPair.getPublicKey().toString(16));
            Credentials credentials = Credentials.create(web3JKeyPair.getPrivateKey());
            String address=credentials.getAddress();
            web3JKeyPair.setAddress(address);
        }catch (Exception e){
            e.printStackTrace();
        }
        return web3JKeyPair;
    }

    public static String  toWeb3Address(String address){
        byte[] addressBytes= Hex.decode(address);
        byte[] web3AddressBytes = Arrays.copyOfRange(addressBytes, addressBytes.length-20, addressBytes.length);
        String web3Address= "0x"+Hex.toHexString(web3AddressBytes);
        return web3Address;
    }
}
