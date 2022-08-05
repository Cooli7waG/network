package com.aitos.xenon.device.mapper;

import com.aitos.xenon.device.api.domain.dto.ClaimDto;
import com.aitos.xenon.device.domain.AirDropRecord;
import org.apache.ibatis.annotations.Param;

public interface AirDropRecordMapper {

    void save(AirDropRecord airDropRecord);

    void claim(ClaimDto claimDto);

    AirDropRecord  findNotClaimedByMinerAddress(@Param("minerAddress")String minerAddress);

    AirDropRecord findByMinerAddress(@Param("minerAddress")String minerAddress);
}
