package com.aitos.xenon.account.service;

import com.aitos.xenon.account.api.domain.dto.AccountSearchDto;
import com.aitos.xenon.account.api.domain.vo.AccountVo;
import com.aitos.xenon.account.domain.Account;
import com.aitos.xenon.core.model.Page;
import com.aitos.xenon.core.model.QueryParams;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public interface AccountService {
    void save(Account account);

    void update(Account account);

    Account findByAddress(String address);

    IPage<AccountVo> list(AccountSearchDto accountSearchDto);

    List<Account> findListByIds(List<Long> idsList);

    void updateBalance(Account account);

    BigInteger bmtCirculation();
}
