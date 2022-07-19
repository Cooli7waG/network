package com.aitos.xenon.block.service;

import com.aitos.xenon.block.domain.PoggChallenge;
import com.aitos.xenon.block.domain.PoggChallengeRecord;

import java.util.List;

public interface PoggService {
    void genChallenge();

    /**
     * 查询当前处于活跃状态的PoGG挑战
     */
    List<PoggChallenge> activeChallenges();

    /**
     * 保存挑战成功的记录
     * @param poggChallengeRecord
     */
    String saveChallengeRecord(PoggChallengeRecord poggChallengeRecord);

    /**
     * 查询记录是否存在
     */
    PoggChallengeRecord findChallengeRecordByRandom(String address,String random);

    /**
     * 用于进行PoGG奖励
     */
     void reward();

    PoggChallenge queryChallenges(String txHash);
}
