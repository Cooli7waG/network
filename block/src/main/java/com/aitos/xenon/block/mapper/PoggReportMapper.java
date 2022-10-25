package com.aitos.xenon.block.mapper;

import com.aitos.xenon.block.api.domain.dto.PoggReportDto;
import com.aitos.xenon.block.api.domain.dto.PoggReportSearchDto;
import com.aitos.xenon.block.api.domain.vo.PoggReportDataVo;
import com.aitos.xenon.block.domain.PoggReportPowerData;
import com.aitos.xenon.block.domain.PoggReportSubtotal;
import com.aitos.xenon.block.domain.PoggReportSubtotalStatistics;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface PoggReportMapper {
    /**
     * 批量保存report记录
     * @param poggReportDto
     */
    void batchSave(PoggReportDto poggReportDto);


    void saveSubtotal(PoggReportSubtotal poggReportSubtotal);

    long findTotalEnergyGeneration(@Param("startTime")LocalDateTime startTime,@Param("endTime")LocalDateTime endTime,@Param("ownerAddress")String ownerAddress);

    IPage<PoggReportDataVo> findReportDataListByPage(@Param("page")Page<PoggReportDataVo> page, @Param("queryParams")PoggReportSearchDto queryParams);

    void updateSubtotal(PoggReportSubtotal poggReportSubtotal);

    PoggReportSubtotal findSubtotalByEpoch(@Param("address") String address, @Param("epoch") Long epoch);

    List<PoggReportSubtotalStatistics> findSubtotalStatisticsList(@Param("startEpoch")long startEpoch,@Param("endEpoch") long endEpoch);

    List<PoggReportPowerData> findPowerDataListByEpoch(@Param("address")String address, @Param("startEpoch")long startEpoch,@Param("endEpoch") long endEpoch);

    double avgPower(@Param("address")String address);

    /**
     * 根据Miner地址获取最后一条Report数据
     * @param address
     * @return
     */
    PoggReportDataVo lastReport(@Param("address")String address);
}
