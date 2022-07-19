package com.aitos.xenon.device.service.impl;

import com.aitos.xenon.device.domain.DeviceDetial;
import com.aitos.xenon.device.mapper.DeviceDetialMapper;
import com.aitos.xenon.device.service.DeviceDetialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceDetialServiceImpl implements DeviceDetialService {
    @Autowired
    private DeviceDetialMapper deviceDetialMapper;
    @Override
    public void save(DeviceDetial deviceDetial) {
        deviceDetialMapper.save(deviceDetial);
    }

    @Override
    public DeviceDetial findByDeviceId(Long deviceId) {
        return deviceDetialMapper.findByDeviceId(deviceId);
    }

    @Override
    public void updateDeviceDetial(DeviceDetial  deviceDetial) {
        deviceDetialMapper.update(deviceDetial);
    }
}
