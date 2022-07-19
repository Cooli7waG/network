package com.aitos.xenon.account.service;

import com.aitos.blockchain.web3j.BmtERC20;
import com.aitos.blockchain.web3j.We3jUtils;
import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;

@SpringBootTest
public class ContractTest {

    @Autowired
    private BmtERC20 bmtERC20;

    @Test
    void test_address() throws Exception {
        System.out.println(We3jUtils.genKeyPair());
    }

    @Test
    public void test_query(){
       /* AbiObject abiObject=anchorCurrencyContract.getAbiObject("balanceOf");

        List<String> inputList = new ArrayList<>();
        inputList.add("cb23252e6f49c497d9c9367fcf973da4fb165b4b66f2f67b33d3aa26b3dbeb01");
        Function function = abiObject.getFunction(inputList);
        RemoteCall query = anchorCurrencyContract.query(function);
        try {
            DynamicBytes bytes = (DynamicBytes) query.sendAsync().get();
            System.out.println(new String(bytes.getValue()));
        }catch (Exception e){
            e.printStackTrace();
        }*/
    }

    @Test
    public void test_balanceOf() {

        try {
            BigInteger balanceOf = bmtERC20.balanceOf("380BE9A0B62A99CFF726F1DCB3BF63C1E1B66219F965FA446A03154A525D3397").send();
            System.out.println(balanceOf);

            System.out.println("balanceOf=" + balanceOf);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_transfer() throws Exception {

        BigInteger bmtCirculation = bmtERC20.alreadyReward().send();
        BigDecimal fee = Convert.fromWei(new BigDecimal(bmtCirculation), Convert.Unit.ETHER);
        System.out.println(fee);

        /*BigDecimal fee = Convert.toWei(new BigDecimal("1000000000000000000000"), Convert.Unit.ETHER);
       Object result= bmtERC20.rewardMiner("380BE9A0B62A99CFF726F1DCB3BF63C1E1B66219F965FA446A03154A525D3397",fee.toBigInteger()).send();
        System.out.println(result);*/
       /*try {
            BigDecimal fee = Convert.toWei(new BigDecimal("10"), Convert.Unit.ETHER);
            TransactionReceipt  transactionReceipt= bmtERC20.transfer("0xcf973da4fb165b4b66f2f67b33d3aa26b3dbeb01",fee.toBigInteger()).send();
            System.out.println("balanceOf=" + JSON.toJSONString(transactionReceipt));
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}
