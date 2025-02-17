package com.aitos.xenon.account.service;

import com.aitos.common.crypto.coder.DataCoder;
import com.aitos.common.crypto.ecdsa.Ecdsa;
import com.aitos.xenon.account.api.domain.vo.AccountVo;
import com.aitos.xenon.account.domain.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.codec.Hex;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

@SpringBootTest
public class AccountServiceTest {


    @Autowired
    private AccountService  accountService;

    @Value("${foundation.privateKey}")
    private String privateKey;

    @Value("${foundation.publicKey}")
    private String publicKey;

    @Test
    public void test_save_sign(){
        String data="11AN9qPdD9fCWQKkhpkUxe6jeVw1xA8Q4otZfhbZK7P6ek";
        String sign= Ecdsa.sign(privateKey,data, DataCoder.BASE58);
        System.out.println(sign);

        Boolean verify=Ecdsa.verifyByPublicKey(publicKey,data,sign, DataCoder.BASE58);
        System.out.println(verify);
    }

    @Test
    public void test_save_sign2(){
        String data="{\"version\":1,\"minerAddress\":\"1162eqbyXo8p4JPxbLVt94e8zL8TVgyqyeF9Ytg7PhC55o\",\"ownerAddress\":\"11D1gq3XVrkp2UEgJ25afSdEZuoncyz9JngBfKZjfjQxRW\",\"location\":{\"version\":1,\"locationType\":0,\"latitude\":0,\"longitude\":0},\"minerInfo\":{\"version\":1,\"energy\":0,\"capabilities\":1,\"power\":10,\"deviceModel\":\"AD40\",\"deviceSerialNum\":\"100100101\"}}";
        String sign= Ecdsa.sign(privateKey,data, DataCoder.BASE58);
        System.out.println(sign);

        Boolean verify=Ecdsa.verifyByPublicKey(publicKey,data.getBytes(),sign, DataCoder.BASE58);
        System.out.println(verify);
    }

    @Test
    public void test(){
        String tt="CB23252E6F49C497D9C9367FCF973DA4FB165B4B66F2F67B33D3AA26B3DBEB01";
        byte[] bytes=Hex.decode(tt);
        byte[] newScores = Arrays.copyOfRange(bytes, bytes.length-20, bytes.length);

        System.out.println(newScores.length);
        System.out.println(Hex.encode(newScores));
    }

    @Test
    public void test2(){
      /*BigDecimal a1=  Convert.fromWei("500000000000000000000", Convert.Unit.ETHER);
        System.out.println(a1);

        BigDecimal a2=  Convert.toWei(a1, Convert.Unit.ETHER);
        System.out.println(a2);*/
    }

    @Test
    public void test_save(){
        Account  account=new Account();
        account.setAddress("fdsafasfasdfasfasdfasdfasfasfasfasfas");
        account.setAccountType(1);
        account.setBalance(new BigInteger("32.5").toString());
        account.setNonce(1l);
        accountService.save(account);
    }

    @Test
    public void test_update(){
        Account  account=new Account();
        account.setAddress("fdsafasfasdfasfasdfasdfasfasfasfasfas");
        account.setAccountType(1);
        account.setBalance(new BigInteger("37.5").toString());
        account.setNonce(1l);
        accountService.update(account);
    }


    @Test
    public void test_findByAddress(){

        /*AccountVo byAddress = accountService.findByAddress("0xefe22f370d03ace02f82e379a754b072123e4db2");
        System.out.println(byAddress);*/
    }


}
