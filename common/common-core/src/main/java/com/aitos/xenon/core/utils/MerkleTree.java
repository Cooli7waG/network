package com.aitos.xenon.core.utils;

import org.bouncycastle.jcajce.provider.digest.SHA256;
import org.bouncycastle.util.encoders.Hex;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class MerkleTree {

    public static String merkleTree(List<String> hashList) {
        if(hashList==null||hashList.size()==0){
            return getSHA256("00000000000000000000000000000000");
        }
        int levelOffset = 0;
        for (int levelSize = hashList.size(); levelSize > 1; levelSize = (levelSize + 1) / 2) {
            for (int left = 0; left < levelSize; left += 2) {
                int right = Math.min(left + 1, levelSize - 1);
                String tleft = hashList.get(levelOffset + left);
                String tright = hashList.get(levelOffset + right);
                hashList.add( getSHA256(tleft + tright));
            }
            levelOffset += levelSize;
        }
        return hashList.get(hashList.size()-1);
    }

    private static String getSHA256(String str) {
        MessageDigest messageDigest;
        String encodestr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodestr = Hex.toHexString(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodestr;
    }

}
