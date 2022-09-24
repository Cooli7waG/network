package com.aitos.xenon.account.service;

import com.aitos.blockchain.web3j.Erc20Service;
import com.aitos.blockchain.web3j.We3jUtils;
import com.aitos.common.crypto.ecdsa.Ecdsa;
import com.aitos.common.crypto.ecdsa.EcdsaKeyPair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class ContractTest {

    @Autowired
    private Erc20Service erc20Service;

    public static void main(String[] args) {
        LocalDateTime utc = Instant.ofEpochMilli(new Date().getTime()).atZone(ZoneId.of("UTC")).toLocalDateTime();
        //System.out.println(utc.toInstant().toEpochMilli());
        System.out.println(utc.toInstant(ZoneOffset.UTC).toEpochMilli());

        System.out.println(LocalDateTime.now());

        System.out.println(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());


        LocalDateTime todayStart=LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime todayEnd=LocalDateTime.now();

        System.out.println(todayStart);
        System.out.println(todayEnd);
    }

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

    @Autowired
    private Web3j web3j;
    @Value("${web3.contractAddress}")
    private String contractAddress;

    @Value("${foundation.web3PrivateKey}")
    private String web3PrivateKey;

    @Value("${foundation.web3PublicKey}")
    private String web3PublickKey;

    @Test
    public void test_nonce() {
        try {
            EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                    contractAddress, DefaultBlockParameterName.LATEST).send();
            BigInteger nonce = ethGetTransactionCount.getTransactionCount();
            System.out.println("nonce=" + nonce);

            Credentials credentials = Credentials.create(web3PrivateKey);


            BigInteger value = Convert.toWei("1.0", Convert.Unit.ETHER).toBigInteger();
            BigInteger gasPrice = BigInteger.valueOf(0);
            BigInteger gasLimit = BigInteger.valueOf(0);

            RawTransaction rawTransaction  = RawTransaction.createEtherTransaction(
                    nonce, gasPrice, gasLimit,
                    "0x0a81121ad13a7f5974a2237c76c949179e088ace",
                    value);
            //sign
            byte[] txSignedBytes = TransactionEncoder.signMessage(rawTransaction, credentials);
            String txSigned = Numeric.toHexString(txSignedBytes);
            //send
            EthSendTransaction ethSendTx = web3j
                    .ethSendRawTransaction(txSigned)
                    .send();
            String txHash = ethSendTx.getTransactionHash();
            System.out.println(txHash);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_balanceOf() {
        try {
            EcdsaKeyPair ecdsaKeyPair = Ecdsa.genKeyPair();

            BigInteger balanceOf = erc20Service.balanceOf("0x0a81121ad13a7f5974a2237c76c949179e088ace").send();
            System.out.println(balanceOf);

            System.out.println("balanceOf=" + balanceOf);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_rewardMiner_multi() {
        try {
            EcdsaKeyPair ecdsaKeyPair = Ecdsa.genKeyPair();

            String address=Ecdsa.getAddress(ecdsaKeyPair.getPublicKey());
            List<String> list=new ArrayList<>();
            list.add(address);
            List<BigInteger> list2=new ArrayList<>();
            list2.add(BigInteger.valueOf(100));
            TransactionReceipt send = erc20Service.rewardMiner_multi(list, list2).send();
            System.out.println(send);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_transfer() throws Exception {

        BigInteger bmtCirculation = erc20Service.alreadyReward().send();
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
