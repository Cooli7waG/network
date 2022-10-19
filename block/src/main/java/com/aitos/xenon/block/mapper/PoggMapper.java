package com.aitos.xenon.block.mapper;

import com.aitos.xenon.block.domain.PoggCommit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

    PoggCommit findByEpoch(@Param("epoch") Long epoch);
}
