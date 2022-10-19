package com.aitos.xenon.block.api.domain.vo;

import lombok.Data;

@Data
public class SystemConfigVo {

    /**
     * commit epoch
     */
    private int poggCommitEpoch;
    /**
     *取数据的范围,默认一周
     */
    private int calDataRange;
    /**
     * 每60个块有资格获得1次奖励
     */
    private int perRewardBlocks;
    /**
     * 每个周期奖励token数量
     */
    private float perEpochTokenNumber;
    /**
     * 用户申领GameMiner的有效期为1周
     */
    private int gameMinerApplyValidityPeriod;

    /**
     * GameMiner总数量限制为20000个
     */
    private int gameMinerTotalNumber;

    /**
     * 单个用户申请的GameMiner数量为1个
     */
    private int gameMinerSingleNumber;
}
