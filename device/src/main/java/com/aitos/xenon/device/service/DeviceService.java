package com.aitos.xenon.device.service;

import com.aitos.xenon.device.api.domain.dto.DeviceBindDto;
import com.aitos.xenon.device.api.domain.dto.DeviceRegisterDto;
import com.aitos.xenon.device.api.domain.dto.DeviceSearchDto;
import com.aitos.xenon.device.api.domain.dto.DeviceTerminateMinerDto;
import com.aitos.xenon.device.api.domain.vo.DeviceVo;
import com.aitos.xenon.device.domain.Device;
import com.aitos.xenon.device.domain.DeviceDetial;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

public interface DeviceService {

    void save(DeviceRegisterDto deviceRegister);

    Device findByAddress(String address);

    void bind(DeviceBindDto deviceBindDto);

    void update(Device  device);

    IPage<DeviceVo> list(DeviceSearchDto queryParams);

    List<Device> queryByOwner(String ownerAddress);

    DeviceVo queryByMiner(String minerAddress);

    void terminate(DeviceTerminateMinerDto deviceTerminateMinerDto);
}
