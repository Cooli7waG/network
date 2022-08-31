package com.aitos.xenon.block.mapper;

import com.aitos.xenon.block.api.domain.dto.PoggReportDto;
import com.aitos.xenon.block.domain.PoggReportSubtotal;
import com.aitos.xenon.block.domain.PoggReportSubtotalStatistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PoggReportMapper {
    /**
     * 批量保存report记录
     * @param poggReportDto
     */
    void batchSave(PoggReportDto poggReportDto);


    void saveSubtotal(PoggReportSubtotal poggReportSubtotal);

    void updateSubtotal(PoggReportSubtotal poggReportSubtotal);

    PoggReportSubtotal findSubtotalByEpoch(@Param("address") String address, @Param("epoch") Long epoch);

    List<PoggReportSubtotalStatistics> findSubtotalStatisticsList(@Param("startEpoch")long startEpoch,@Param("endEpoch") long endEpoch);

    double avgPower(@Param("ownerAddress")String ownerAddress);
}
