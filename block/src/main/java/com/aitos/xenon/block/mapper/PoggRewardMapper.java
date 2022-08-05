package com.aitos.xenon.block.mapper;

import com.aitos.xenon.block.domain.PoggReward;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PoggRewardMapper {
    /**
     * 保存奖励计算记录
     * @param poggReward
     */
    void save(PoggReward poggReward);

    /**
     * 更新奖励发放记录
     * @param status
     * @param msg
     * @param updateTime
     */
    void updateStatus(@Param("id")Long id,@Param("status") Integer status,@Param("msg") String msg, @Param("updateTime")LocalDateTime  updateTime);

    /**
     * 查询未发放的奖励记录
     * @return
     */
    List<PoggReward> findListUnIssued();
}
