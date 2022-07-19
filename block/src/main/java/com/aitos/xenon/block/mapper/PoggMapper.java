package com.aitos.xenon.block.mapper;

import com.aitos.xenon.block.domain.PoggChallenge;
import com.aitos.xenon.block.domain.PoggChallengeRecord;
import com.aitos.xenon.block.domain.PoggChallengeRecordCount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PoggMapper {

    void genChallenge(PoggChallenge pogg);

    List<PoggChallenge> activeChallenges(@Param("height") Long height);


    void saveChallengeRecord(PoggChallengeRecord poggChallengeRecord);

    PoggChallengeRecord findChallengeRecordByRandom(@Param("address")String address,@Param("random") String random);

    List<PoggChallengeRecordCount> findNoRewardChallengeList();

    void updatePoggChallengeRecordStatus(@Param("idList") List<Long> idList);

    PoggChallenge queryChallenges(@Param("txHash")String txHash);
}
