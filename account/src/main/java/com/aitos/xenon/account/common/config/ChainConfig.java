package com.aitos.xenon.account.common.config;


import com.aitos.blockchain.web3j.Erc20Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

import java.io.IOException;
import java.math.BigInteger;


/**
 * 功能：手动初始化Contract对象
 *
 * @Author xymoc
 * @Date 2022年5月6日11:04:09
 */
@Configuration
public class ChainConfig {

    @Value("${web3.url}")
    private String web3Url;

    @Value("${web3.contractAddress}")
    private String contractAddress;

    @Value("${foundation.web3PrivateKey}")
    private String web3PrivateKey;

    @Value("${foundation.web3PublicKey}")
    private String web3PublickKey;



    @Bean
    @Scope("prototype")
    public Erc20Service erc20Service() throws IOException {

        Web3j web3j = Web3j.build(new HttpService(web3Url));
        Credentials credentials = Credentials.create(web3PrivateKey);

        BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();

        ContractGasProvider myGasProvider = new DefaultGasProvider() {
            @Override
            public BigInteger getGasPrice(String contractFunc) {
                return gasPrice;
            }

            @Override
            public BigInteger getGasLimit(String contractFunc) {
                return BigInteger.valueOf(6721975);
            }
        };

        Erc20Service contract = Erc20Service.load(contractAddress, web3j, credentials, myGasProvider);
        return contract;
    }

}
