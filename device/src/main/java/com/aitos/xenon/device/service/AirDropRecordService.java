package com.aitos.xenon.device.service;

import com.aitos.xenon.device.api.domain.dto.AirDropDto;
import com.aitos.xenon.device.api.domain.dto.ClaimDto;
import com.aitos.xenon.device.domain.AirDropRecord;

public interface AirDropRecordService {

    void save(AirDropDto airDropDto);

    void claim(ClaimDto claimDto);

    AirDropRecord  findNotClaimedByMinerAddress(String minerAddress);

    AirDropRecord findByMinerAddress(String minerAddress);

    /**
     * 解析公钥并生成其他数据
     * @param applyGameMiner
     * @return
     */
    String applyGameMiner(String applyGameMiner);

    /**
     * 申领gaming miner
     * @param claimGameMiner
     * @return
     */
    String claimGameMiner(String claimGameMiner);

    /**
     * 手机版申请GamingMiner
     * @param applyGameMiner
     * @return
     */
    String applyGameMinerWithMobile(String applyGameMiner);
}
