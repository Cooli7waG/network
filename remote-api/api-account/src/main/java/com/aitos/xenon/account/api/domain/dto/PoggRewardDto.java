package com.aitos.xenon.account.api.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    private String rewardsJson;

    /**
     * 0.表示未发放，1表已发放，2表示发放失败
     */
    @JsonIgnore
    private Integer status;

    @JsonIgnore
    private String msg;

    private Long height;
}
