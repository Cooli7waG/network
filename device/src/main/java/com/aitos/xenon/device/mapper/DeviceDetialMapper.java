package com.aitos.xenon.device.mapper;

import com.aitos.xenon.device.domain.DeviceDetial;

public interface DeviceDetialMapper {

    void save(DeviceDetial deviceDetial);

    DeviceDetial findByDeviceId(Long deviceId);

    void update(DeviceDetial  deviceDetial);
}
