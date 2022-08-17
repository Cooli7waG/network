package com.aitos.xenon.account.service.impl;

import com.aitos.blockchain.web3j.Erc20Service;
import com.aitos.xenon.account.api.domain.dto.AccountSearchDto;
import com.aitos.xenon.account.api.domain.dto.PoggRewardDetailDto;
import com.aitos.xenon.account.api.domain.vo.AccountVo;
import com.aitos.xenon.account.api.domain.vo.BmtStatisticsVo;
import com.aitos.xenon.account.domain.Account;
import com.aitos.xenon.account.mapper.AccountMapper;
import com.aitos.xenon.account.service.AccountService;
import com.aitos.xenon.common.crypto.Algorithm;
import com.aitos.xenon.common.crypto.XenonCrypto;
import com.aitos.xenon.core.constant.BusinessConstants;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private Erc20Service erc20Service;

    @Override
    public void save(Account account) {
        accountMapper.save(account);
    }

    @Override
    public void update(Account account) {
        accountMapper.update(account);
    }

    @Override
    public Account findByAddress(String address) {
        try {
            Account account = accountMapper.findByAddress(address);
            if (account == null || account.getAccountType().equals(BusinessConstants.AccountType.MINER)) {
                return null;
            } else if (account.getAccountType() == BusinessConstants.AccountType.NETWORK) {
                BigInteger balance = erc20Service._foundations_initial_supply().send();
                account.setBalance(balance.toString());
            } else {
                //TODO 地址需要解码
                String publicKey = XenonCrypto.convertorPublicKey(account.getAddress()).getPublicKey();
                BigInteger blance = erc20Service.balanceOf(XenonCrypto.getAddress(publicKey)).send();
                account.setBalance(blance.toString());
            }
            return account;
        } catch (Exception e) {
            log.error("error:{}", e);
        }
        return null;
    }

    @Override
    public IPage<AccountVo> list(AccountSearchDto accountSearchDto) {
        try {
            Page<AccountVo> page = new Page<AccountVo>(accountSearchDto.getOffset(), accountSearchDto.getLimit());
            IPage<AccountVo> pageResult = accountMapper.list(page, accountSearchDto);
            List<String> addressList = pageResult.getRecords().stream()
                    .filter(item -> XenonCrypto.convertorPublicKey(item.getAddress()).getAlgorithm().equals(Algorithm.ECDSA))
                    .map(AccountVo::getAddress)
                    .collect(Collectors.toList());
            List<String> ecdsaAddressList = addressList.stream().map(publickey -> XenonCrypto.getAddress(publickey)).collect(Collectors.toList());
            List<BigInteger> list = erc20Service.balanceOf_multi(ecdsaAddressList).send();
            for (AccountVo accountVo : pageResult.getRecords()) {
                Algorithm algorithm = XenonCrypto.convertorPublicKey(accountVo.getAddress()).getAlgorithm();
                if (algorithm.equals(Algorithm.ECDSA)) {
                    if (accountVo.getAccountType() == BusinessConstants.AccountType.NETWORK) {
                        BigInteger balance = erc20Service._foundations_initial_supply().send();
                        accountVo.setBalance(balance.toString());
                    } else {
                        for (int i = 0; i < addressList.size(); i++) {
                            String publickey = addressList.get(i);
                            if (publickey.equals(accountVo.getAddress())) {
                                accountVo.setBalance(list.get(i).toString());
                                break;
                            }
                        }
                    }
                } else {
                    accountVo.setBalance("0");
                }
            }
            return pageResult;
        } catch (Exception e) {
            log.error("error:{}", e);
        }
        return null;
    }

    @Override
    public List<Account> findListByIds(List<Long> idsList) {
        return accountMapper.findListByIds(idsList);
    }

    @Override
    public void updateBalance(Account account) {
        accountMapper.updateBalance(account);
    }

    @Override
    public BigInteger bmtCirculation() {
        try {
            BigInteger bmtCirculation = erc20Service.alreadyReward().send();
            return bmtCirculation;
        } catch (Exception e) {
            log.error("bmtCirculation,error:{}", e);
        }
        return new BigInteger("0");
    }

    @Override
    public BmtStatisticsVo bmtStatistics() {
        BmtStatisticsVo bmtStatisticsVo = new BmtStatisticsVo();
        try {
            BigInteger totalBMTMarket = erc20Service._foundations_initial_supply().send();
            bmtStatisticsVo.setTotalBMTMarket(totalBMTMarket.toString());
            BigInteger bmtCirculation = erc20Service.alreadyReward().send();
            bmtStatisticsVo.setTokenSupply(bmtCirculation.toString());
        } catch (Exception e) {
            log.error("bmtStatistics,error:{}", e);
        }
        return bmtStatisticsVo;
    }

    @Override
    public void updateEarning(List<PoggRewardDetailDto> rewards) {
        accountMapper.updateEarning(rewards);
    }
}
