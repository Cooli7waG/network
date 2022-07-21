package com.aitos.xenon.device.service;

import com.aitos.xenon.device.api.domain.dto.DeviceDetialDto;
import com.aitos.xenon.device.api.domain.dto.DeviceTerminateMinerDto;
import com.aitos.xenon.device.domain.DeviceDetial;

public interface DeviceDetialService {

    void save(DeviceDetial deviceDetial);

    DeviceDetial findByDeviceId(Long deviceId);

    void updateDeviceDetial(DeviceDetial  deviceDetial);

}
