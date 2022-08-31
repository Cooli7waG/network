package com.aitos.xenon.block.service;

import com.aitos.xenon.block.api.domain.dto.PoggReportDto;
import com.aitos.xenon.block.domain.PoggReportSubtotal;
import com.aitos.xenon.block.domain.PoggReportSubtotalStatistics;

import java.util.List;

public interface PoggReportService {
    /**
     * 批量保存report记录
     */
    String reportSave(PoggReportDto poggReportDto);


    void saveOrUpdate(PoggReportSubtotal poggReportSubtotal);

    /**
     * 统计指定范围内的miner 上报记录数
     * @param startEpoch
     * @param endEpoch
     * @return
     */
    List<PoggReportSubtotalStatistics> findSubtotalStatisticsList(long startEpoch,long endEpoch);

    /**
     * 计算平均发电功率
     * @return
     */
    double avgPower(String ownerAddress);
}
