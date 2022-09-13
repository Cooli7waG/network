package com.aitos.xenon.device.service;

import com.aitos.xenon.core.utils.HttpClientUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.security.Key;
import java.security.MessageDigest;

public class ShineMonitorOpenApiTest {
    /**
     * shinemonitor开放平台API访问地址.
     */
    private static String OPEN_API_URI = "http://api.shinemonitor.com/public/";
    private static String usr = "vplant";
    private static String pwd = "vplant";
    private static String companyKey = "xUGjW7Cb7BDq6gWy";
    private static String source = "0";
    private static String _app_id_ = "com.demo.test";
    private static String _app_version_ = "3.6.2.1";
    private static String _app_client_ = "android";

    public static void main(String[] args) throws IOException, InterruptedException {
        //
        String auth = HttpClientUtils.doGet(auth());
        System.out.println("auth result:"+auth);
        JSONObject jsonObject = JSON.parseObject(auth);
        if(jsonObject.getInteger("err") != 0){
            System.out.println("auth failed:"+jsonObject.getString("desc"));
            return;
        }
        //
        JSONObject dat = jsonObject.getJSONObject("dat");
        String secret = dat.getString("secret");
        Long expire = dat.getLong("expire");
        String token = dat.getString("token");
        //
        String s = queryPlantActiveOuputPowerCurrent(token,secret);
        System.out.println("url:"+s);
        String queryPlantActiveOuputPowerCurrent = HttpClientUtils.doGet(s);
        System.out.println("queryPlantActiveOuputPowerCurrent result:"+queryPlantActiveOuputPowerCurrent);//
        //
        String queryPlantEnergyTotal = queryPlantEnergyTotal(token,secret);
        System.out.println("url:"+queryPlantEnergyTotal);
        String queryPlantEnergyTotalResult = HttpClientUtils.doGet(queryPlantEnergyTotal);
        System.out.println("queryPlantEnergyTotal result:"+queryPlantEnergyTotalResult);
    }

    private static String queryPlantEnergyTotal(String token,String secret){
        String salt = System.currentTimeMillis() + "";
        String action = "&action=queryPlantEnergyTotal&plantid=1&source=" + source + "&_app_id_="
                + _app_id_ + "&_app_version_=" + _app_version_
                + "&_app_client_=" + _app_client_;
        String sign = ShineMonitorOpenApiTest.sha1ToLowerCase((salt + secret + token + action).getBytes());
        return ShineMonitorOpenApiTest.OPEN_API_URI + "?sign=" + sign + "&salt=" + salt +"&token="+token+ action;
    }

    private static String queryPlantActiveOuputPowerCurrent(String token,String secret){
        String salt = System.currentTimeMillis() + "";
        String action = "&action=queryPlantActiveOuputPowerCurrent&plantid=1&source=" + source + "&_app_id_="
                + _app_id_ + "&_app_version_=" + _app_version_
                + "&_app_client_=" + _app_client_;
        String sign = ShineMonitorOpenApiTest.sha1ToLowerCase((salt + secret + token + action).getBytes());
        return ShineMonitorOpenApiTest.OPEN_API_URI + "?sign=" + sign + "&salt=" + salt +"&token="+token+ action;
    }

    /**
     * 认证接口.
     */
    private static final String auth() {
        String salt = System.currentTimeMillis() + "";
        String sha1Pwd = ShineMonitorOpenApiTest.sha1ToLowerCase(pwd.getBytes());
        String action = "&action=auth&usr=" + usr  + "&company-key=" + companyKey
                + "&source=" + source + "&_app_id_=" + _app_id_ + "&_app_version_=" + _app_version_ + "&_app_client_=" + _app_client_;
        String sign = ShineMonitorOpenApiTest.sha1ToLowerCase((salt + sha1Pwd + action).getBytes());
        return ShineMonitorOpenApiTest.OPEN_API_URI + "?sign=" + sign + "&salt=" + salt + action;
    }

    /**
     * 注册电站业主账号接口.
     */
    private static final void reg() {
        String usr = "plant-user-01";
        String pwd = "plant-user-01";
        String mobile = "15889700000";
        String email = "eybond@eybond.com";
        String pn = "0123456789ABCD";
        String sn = pn;
        String companyKey = "0123456789ABCDEF";
        String source = "0";
        String _app_id_ = "com.demo.test";
        String _app_version_ = "3.6.2.1";
        String _app_client_ = "android";
        String salt = System.currentTimeMillis() + ""; /* 盐值. */
        String pwdSha1 = ShineMonitorOpenApiTest.sha1ToLowerCase(pwd.getBytes()); /* SHA-1(PWD). */
        String pnSha1 = ShineMonitorOpenApiTest.sha1ToLowerCase(pn.getBytes()); /* SHA-1(PN). */
        byte[] rc4 = ShineMonitorOpenApiTest.rc4enc(pnSha1.getBytes(), pwdSha1.getBytes(), 0, pwdSha1.getBytes().length); /* RC4(SHA-1(PN), SHA-1(PWD)). */
        String action = "&action=reg&usr=" + usr /* 注意: 中文需URLEncoder.encode. */ + "&pwd=" + ShineMonitorOpenApiTest.byte2HexStr(rc4, 0, rc4.length).toLowerCase();
        action += "&mobile=" + mobile + "&email=" + email + "&sn=" + sn + "&company-key=" + companyKey + "&source="
                + source + "&_app_id_=" + _app_id_ + "&_app_version_=" + _app_version_ + "&_app_client_=" + _app_client_;
        String sign = ShineMonitorOpenApiTest.sha1ToLowerCase((salt + pwdSha1 + action).getBytes());
        String request = ShineMonitorOpenApiTest.OPEN_API_URI + "?sign=" + sign + "&salt=" + salt + action;
        System.out.println(request);
    }

    /**
     * 认证通过后的业务API接口.
     */
    private static final void authPassed() {
        String secret = "ffa1655ee3726840822063a02ac5017795809b18"; /* 认证通过后的secret与token. */
        String token = "88d22d819e31897eea2d9d5b9f7792cf4065ac5372aad3672f5e4e147cd25b5f";
        String source = "0";
        String _app_id_ = "com.demo.test";
        String _app_version_ = "3.6.2.1";
        String _app_client_ = "android";
        String salt = System.currentTimeMillis() + ""; /* 盐值. */
        String action = "&action=queryCollectorInfo&pn=G0916522248153" + "&source=" + source + "&_app_id_="
                + _app_id_ + "&_app_version_=" + _app_version_ + "&_app_client_=" + _app_client_;
        String sign = ShineMonitorOpenApiTest.sha1ToLowerCase((salt + secret + token + action).getBytes()); /* SHA-1(slat + secret + token + action). */
        String request = ShineMonitorOpenApiTest.OPEN_API_URI + "?sign=" + sign + "&salt=" + salt + "&token=" + token + action;
        System.out.println(request);
    }

    /**
     * 将字节流转换成十六进制字符串(紧凑格式, 无空格).
     */
    private static final String byte2HexStr(byte by[], int ofst, int len) {
        if (len < 1)
            return "";
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(bos);
        for (int i = ofst; i < ofst + len; i++)
            ps.printf("%02X", by[i]);
        return bos.toString();
    }

    /**
     * SHA-1摘要算法.
     */
    private static final String sha1ToLowerCase(byte[] by) {
        try {
            byte dat[] = MessageDigest.getInstance("SHA-1").digest(by);
            return ShineMonitorOpenApiTest.byte2HexStr(dat, 0, dat.length).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * RC4加密.
     */
    public static final byte[] rc4enc(byte key[], byte[] org, int ofst, int len) {
        try {
            Key k = new SecretKeySpec(key, "RC4");
            Cipher cip = Cipher.getInstance("RC4");
            cip.init(Cipher.ENCRYPT_MODE, k);
            return cip.doFinal(org, ofst, len);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
