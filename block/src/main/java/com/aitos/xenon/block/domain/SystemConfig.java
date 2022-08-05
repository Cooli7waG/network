package com.aitos.xenon.block.domain;

import com.aitos.xenon.core.model.BaseModel;
import lombok.Data;

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
    private float gameMinerCoefficient;
    /**
     * api miner 系数
     */
    private float apiMinerCoefficient;
    /**
     * virtual miner系数
     */
    private float virtualMinerCoefficient;
    /**
     * Standard PV Miner 系数
     */
    private float standardPvMinerCoefficient;
    /**
     * Lite PV Miner
     */
    private float litePvMinerCoefficient;
    /**
     * 每个周期奖励token数量
     */
    private float perEpochTokenNumber;
}
