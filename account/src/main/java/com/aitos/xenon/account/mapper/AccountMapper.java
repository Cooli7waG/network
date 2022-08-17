package com.aitos.xenon.account.mapper;

import com.aitos.xenon.account.api.domain.dto.AccountSearchDto;
import com.aitos.xenon.account.api.domain.dto.PoggRewardDetailDto;
import com.aitos.xenon.account.api.domain.vo.AccountVo;
import com.aitos.xenon.account.domain.Account;
import com.aitos.xenon.core.model.QueryParams;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AccountMapper {
    void save(Account account);

    void update(Account account);
    void updateBalance(Account account);

    Account findByAddress(String address);

    IPage<AccountVo> list(Page<AccountVo> page,@Param("queryParams") AccountSearchDto accountSearchDto);

    List<Account> findListByIds(@Param("idsList")List<Long> idsList);

    void updateEarning(@Param("rewards")List<PoggRewardDetailDto> rewards);
}
