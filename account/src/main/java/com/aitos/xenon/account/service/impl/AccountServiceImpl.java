package com.aitos.xenon.account.service.impl;

import com.aitos.blockchain.web3j.Erc20Service;
import com.aitos.xenon.account.api.domain.dto.AccountSearchDto;
import com.aitos.xenon.account.api.domain.dto.PoggRewardDetailDto;
import com.aitos.xenon.account.api.domain.vo.AccountVo;
import com.aitos.xenon.account.api.domain.vo.BmtStatisticsVo;
import com.aitos.xenon.account.domain.Account;
import com.aitos.xenon.account.mapper.AccountMapper;
import com.aitos.xenon.account.service.AccountService;
import com.aitos.xenon.core.constant.BusinessConstants;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
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
        account.setCreateTime(LocalDateTime.now());
        accountMapper.save(account);
    }

    @Override
    public void update(Account account) {
        account.setUpdateTime(LocalDateTime.now());
        accountMapper.update(account);
    }

    @Override
    public AccountVo findByAddress(String address) {
        try {
            AccountVo account = accountMapper.findByAddress(address);
            if (account == null) {
                BigInteger blance = erc20Service.balanceOf(account.getAddress()).send();
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
            List<String> addressList = pageResult.getRecords().stream().map(accountVo -> accountVo.getAddress()).collect(Collectors.toList());
            List<BigInteger> list = erc20Service.balanceOf_multi(addressList).send();
            for (AccountVo accountVo : pageResult.getRecords()) {
                for (int i = 0; i < addressList.size(); i++) {
                    String publickey = addressList.get(i);
                    if (publickey.equals(accountVo.getAddress())) {
                        accountVo.setBalance(list.get(i).toString());
                        break;
                    }
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
        account.setUpdateTime(LocalDateTime.now());
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
        accountMapper.updateEarning(rewards,LocalDateTime.now());
    }
}
