package com.aitos.xenon.device.service.impl;

import com.aitos.xenon.device.domain.ReportDeviceInfo;
import com.aitos.xenon.device.mapper.ReportDeviceInfoMapper;
import com.aitos.xenon.device.service.ReportDeviceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReportDeviceInfoServiceImpl implements ReportDeviceInfoService {

    @Autowired
    private ReportDeviceInfoMapper reportDeviceInfoMapper;

    @Override
    public void save(ReportDeviceInfo reportDeviceInfo) {
        reportDeviceInfo.setLatitude(reportDeviceInfo.getLocation().getLatitude());
        reportDeviceInfo.setLongitude(reportDeviceInfo.getLocation().getLongitude());
        reportDeviceInfo.setLocationType(reportDeviceInfo.getLocation().getLocationType());
        reportDeviceInfo.setH3index(reportDeviceInfo.getLocation().getH3index());
        reportDeviceInfo.setCreateTime(LocalDateTime.now());
        reportDeviceInfoMapper.save(reportDeviceInfo);
    }
}
