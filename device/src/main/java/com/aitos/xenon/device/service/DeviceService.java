package com.aitos.xenon.device.service;

import com.aitos.xenon.core.model.QueryParams;
import com.aitos.xenon.device.api.domain.dto.*;
import com.aitos.xenon.device.domain.Device;
import com.aitos.xenon.device.api.domain.vo.DeviceVo;
import com.aitos.xenon.device.domain.DeviceDetial;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

public interface DeviceService {

    void save(DeviceRegisterDto deviceRegister);

    Device findByAddress(String address);

    void bind(DeviceBindDto deviceBindDto);

    IPage<Device> list(DeviceSearchDto queryParams);

    List<DeviceVo> queryByOwner(String ownerAddress);

    DeviceVo queryByMiner(String minerAddress);

    DeviceDetial findDetialByDeviceId(Long deviceId);

    void terminate(DeviceTerminateMinerDto deviceTerminateMinerDto);
}
