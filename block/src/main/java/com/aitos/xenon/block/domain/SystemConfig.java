package com.aitos.xenon.block.domain;

import com.aitos.xenon.core.model.BaseModel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SystemConfig extends BaseModel {
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
     * game miner系数
     */
    @Deprecated
    private float gameMinerCoefficient;
    /**
     * api miner 系数
     */
    @Deprecated
    private float apiMinerCoefficient;
    /**
     * virtual miner系数
     */
    @Deprecated
    private float virtualMinerCoefficient;
    /**
     * Standard PV Miner 系数
     */
    @Deprecated
    private float standardPvMinerCoefficient;
    /**
     * Lite PV Miner
     */
    @Deprecated
    private float litePvMinerCoefficient;
    /**
     * 每个周期奖励token数量
     */
    private BigDecimal perEpochTokenNumber;
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
