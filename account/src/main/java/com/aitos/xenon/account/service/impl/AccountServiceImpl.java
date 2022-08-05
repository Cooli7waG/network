package com.aitos.xenon.account.service.impl;

import com.aitos.blockchain.web3j.BmtERC20;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private BmtERC20 bmtERC20;

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
        try{
            Account account=accountMapper.findByAddress(address);
            if(account==null){
                return null;
            }else if(account.getAccountType()== BusinessConstants.AccountType.NETWORK){
                BigInteger balance= bmtERC20._foundations_left().send();
                account.setBalance(balance.toString());
            }else{
                BigInteger blance= bmtERC20.balanceOf(account.getAddress()).send();
                account.setBalance(blance.toString());
            }
            return account;
        }catch (Exception e){
            log.error("error:{}",e);
        }
        return null;
    }

    @Override
    public IPage<AccountVo> list(AccountSearchDto accountSearchDto) {
        try{
            Page<AccountVo> page=new Page<AccountVo>(accountSearchDto.getOffset(),accountSearchDto.getLimit());
            IPage<AccountVo> pageResult=accountMapper.list(page,accountSearchDto);
            List<String> addressList=pageResult.getRecords().stream().map(item->item.getAddress()).collect(Collectors.toList());
            List<BigInteger> list= bmtERC20.balanceOf_multi(addressList).send();
            int index=0;
            for(AccountVo accountVo:pageResult.getRecords()){
                if(accountVo.getAccountType()== BusinessConstants.AccountType.NETWORK){
                    BigInteger balance= bmtERC20._foundations_left().send();
                    accountVo.setBalance(balance.toString());
                }else{
                    accountVo.setBalance(list.get(index).toString());
                }
                index++;
            }
            return pageResult;
        }catch (Exception e){
            log.error("error:{}",e);
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
            BigInteger bmtCirculation = bmtERC20.alreadyReward().send();
            return bmtCirculation;
        }catch (Exception e){
            log.error("bmtCirculation,error:{}",e);
        }
        return new BigInteger("0");
    }

    @Override
    public BmtStatisticsVo bmtStatistics() {
        BmtStatisticsVo bmtStatisticsVo=new BmtStatisticsVo();
        try {
            BigInteger totalBMTMarket =bmtERC20._totalSupply().send();
            bmtStatisticsVo.setTotalBMTMarket(totalBMTMarket.toString());
            BigInteger bmtCirculation = bmtERC20.alreadyReward().send();
            bmtStatisticsVo.setTokenSupply(bmtCirculation.toString());
        }catch (Exception e){
            log.error("bmtStatistics,error:{}",e);
        }
        return bmtStatisticsVo;
    }

    @Override
    public void updateEarning(List<PoggRewardDetailDto> rewards) {
        accountMapper.updateEarning(rewards);
    }
}
