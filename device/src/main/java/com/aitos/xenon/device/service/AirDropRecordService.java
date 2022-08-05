package com.aitos.xenon.device.service;

import com.aitos.xenon.device.api.domain.dto.AirDropDto;
import com.aitos.xenon.device.api.domain.dto.ClaimDto;
import com.aitos.xenon.device.domain.AirDropRecord;

public interface AirDropRecordService {

    void save(AirDropDto airDropDto);

    void claim(ClaimDto claimDto);

    AirDropRecord  findNotClaimedByMinerAddress(String minerAddress);

    AirDropRecord findByMinerAddress(String minerAddress);
}
