package com.aitos.xenon.block.mapper;

import com.aitos.xenon.block.domain.PoggCommit;
import com.aitos.xenon.block.domain.PoggChallengeRecord;
import com.aitos.xenon.block.domain.PoggChallengeRecordCount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
