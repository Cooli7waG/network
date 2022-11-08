package com.aitos.xenon.device.service;

import com.aitos.xenon.core.utils.Location;
import com.aitos.xenon.device.api.domain.dto.*;
import com.aitos.xenon.device.api.domain.vo.DeviceVo;
import com.aitos.xenon.device.domain.Device;
import com.aitos.xenon.device.domain.DeviceDetial;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface DeviceService {

    void save(DeviceRegisterDto deviceRegister);

    Device findByAddress(String address);

    void bind(DeviceBindDto deviceBindDto);

    void update(Device  device);

    IPage<DeviceVo> list(DeviceSearchDto queryParams);

    List<Device> queryByOwner(String ownerAddress);

    DeviceVo queryByMiner(String minerAddress);

    void terminate(DeviceTerminateMinerDto deviceTerminateMinerDto);

    /**
     * 根据owner地址查询miner列表
     * @param deviceSearchDto
     * @return
     */
    IPage<DeviceVo> getMinersByOwnerAddress(DeviceSearchDto deviceSearchDto);

    /**
     * 获取所有miner位置信息
     * @return
     */
    Map<String, Location> getMinerLocation();

    /**
     * 更新miner位置
     * @param minerAddress
     * @param latitude
     * @param longitude
     */
    void updateMinerLocation(String minerAddress,String latitude,String longitude);

    List<DeviceVo> loadMinersInfo(ArrayList<String> addressList);

    /**
     * 根据owner查询miner列表
     * @param address
     * @return
     */
    List<DeviceVo> getMinerListByOwner(String address);

    /**
     * 按miner类型统计数量
     * @param minerType
     * @return
     */
    int countByMinerType(int minerType);

    /**
     * 按miner类型统计数量
     * @param ownerAddress
     * @param minerType
     * @return
     */
    int countByAddressAndMinerType(String ownerAddress,int minerType);

    /**
     * 获取所有miner（已绑定）
     * @return
     */
    List<DeviceVo> getAllMiner();

    /**
     * 修改miner 运行状态
     * @param id
     * @param runStatus
     */
    void updateMinerRunStatus(Long id, int runStatus);

}
