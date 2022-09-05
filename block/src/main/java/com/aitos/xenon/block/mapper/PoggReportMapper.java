package com.aitos.xenon.block.mapper;

import com.aitos.xenon.block.api.domain.dto.PoggReportDto;
import com.aitos.xenon.block.api.domain.dto.PoggReportSearchDto;
import com.aitos.xenon.block.api.domain.vo.PoggReportDataVo;
import com.aitos.xenon.block.domain.PoggReportSubtotal;
import com.aitos.xenon.block.domain.PoggReportSubtotalStatistics;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    IPage<PoggReportDataVo> findReportDataListByPage(@Param("page")Page<PoggReportDataVo> page, @Param("queryParams")PoggReportSearchDto queryParams);

    void updateSubtotal(PoggReportSubtotal poggReportSubtotal);

    PoggReportSubtotal findSubtotalByEpoch(@Param("address") String address, @Param("epoch") Long epoch);

    List<PoggReportSubtotalStatistics> findSubtotalStatisticsList(@Param("startEpoch")long startEpoch,@Param("endEpoch") long endEpoch);

    double avgPower(@Param("ownerAddress")String ownerAddress);
}
