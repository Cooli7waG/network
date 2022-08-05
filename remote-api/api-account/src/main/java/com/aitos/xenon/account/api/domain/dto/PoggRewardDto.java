package com.aitos.xenon.account.api.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class PoggRewardDto {

    /**
     * 可验证凭据，私钥
     */
    private String verifiableEvidence;

    private Long startEpoch;

    private Long endEpoch;

    /**
     * 奖励明细
     */
    private List<PoggRewardDetailDto> rewards;

    private String rewardsJson;

    /**
     * 0.表示未发放，1表已发放，2表示发放失败
     */
    private Integer status;

    private String msg;

    private Long height;
}
