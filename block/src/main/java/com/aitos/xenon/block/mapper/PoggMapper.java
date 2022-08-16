package com.aitos.xenon.block.mapper;

import com.aitos.xenon.block.domain.PoggCommit;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PoggMapper {

    void saveCommit(PoggCommit pogg);

    PoggCommit findCurrentCommit();

    void updateCommit(PoggCommit currentPoggCommit);

    /**
     * 查找已结束的commit
     * @return
     */
    PoggCommit findOverCommit();
}
