package com.aitos.xenon.block.domain;

import lombok.Data;
import java.util.List;
import java.util.Map;

import com.aitos.xenon.block.api.domain.dto.PoggGreenDataDto;
import com.aitos.xenon.core.utils.Location;

/**
 * 每个 epoch 每个 miner 的真实性数据
 */
@Data
public class PoggAuthenticity {

    private Long epoch;

    private Long timestamp;

    /**
     * 真实性得分
     */
    private Double score;

    /**
     * 用来识别目标 miner
     */

    private String address;

    /**
     * 目标 miner 地理位置
     */
    private Location location;

    /**
     * 目标 miner 其他指标
     */

    private String weather;

    private Boolean isDaytime;

    /**
     * 目标 miner 在窗口期内的光功率数据
     */
    private List<PoggReportPowerData> powerData;

    /**
     * 对比 miner 在窗口期内的光功率数据
     */
    private Map<String, List<PoggReportPowerData>> compareMinerPowerData;
}
