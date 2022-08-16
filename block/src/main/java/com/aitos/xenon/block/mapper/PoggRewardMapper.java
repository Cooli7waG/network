package com.aitos.xenon.block.mapper;

import com.aitos.xenon.block.api.domain.PoggReport;
import com.aitos.xenon.block.api.domain.PoggRewardMiner;
import com.aitos.xenon.block.api.domain.dto.PoggReportDto;

import com.aitos.xenon.block.domain.PoggReward;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
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

    /**
     * 查询所有奖励记录
     * @return
     */
    List<PoggReward> findAll();

    /**
     * 保存奖励记录
     * @param poggRewardMiner
     */
    void savePoggRewardMiner(PoggRewardMiner poggRewardMiner);

    IPage<PoggReport> getReportByMinerAddress(Page<PoggReport> page, @Param("queryParams")PoggReportDto queryParams);

    IPage<PoggRewardMiner> getRewardByMinerAddress(Page<PoggRewardMiner> page, @Param("queryParams")PoggReportDto queryParams);
}
