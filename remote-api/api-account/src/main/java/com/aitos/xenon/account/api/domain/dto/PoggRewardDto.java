package com.aitos.xenon.account.api.domain.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class PoggRewardDto {

    /**
     * 可验证凭据，私钥
     */
    private String verifiableEvidence;

    @JSONField(serialize = false)
    private Long startEpoch;

    @JSONField(serialize = false)
    private Long endEpoch;

    private Long epoch;

    /**
     * 奖励明细
     */
    private List<PoggRewardDetailDto> rewards;

    @JSONField(serialize = false)
    private String rewardsJson;

    /**
     * 0.表示未发放，1表已发放，2表示发放失败
     */
    @JSONField(serialize = false)
    private Integer status;

    @JSONField(serialize = false)
    private String msg;

    private Long height;

    public Long getEpoch() {
        return endEpoch;
    }
}
