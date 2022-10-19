package com.aitos.xenon.block.domain;

import com.aitos.xenon.core.model.BaseModel;
import lombok.Data;

import java.util.List;

@Data
public class PoggReward extends BaseModel {

    /**
     * 可验证凭据，私钥
     */
    private String verifiableEvidence;

    private Long startEpoch;

    private Long endEpoch;

    /**
     * 奖励明细
     */
    private List<PoggRewardDetail> rewards;

    private String rewardsJson;

    /**
     * 0.表示未发放，1表已发放，2表示发放失败
     */
    private Integer status;

    private String msg;

    private Long height;
}
