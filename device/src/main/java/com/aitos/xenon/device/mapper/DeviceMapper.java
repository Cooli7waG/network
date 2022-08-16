package com.aitos.xenon.device.mapper;

import com.aitos.xenon.device.api.domain.dto.DeviceSearchDto;
import com.aitos.xenon.device.api.domain.dto.DeviceTerminateMinerDto;
import com.aitos.xenon.device.api.domain.vo.DeviceVo;
import com.aitos.xenon.device.domain.Device;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeviceMapper {

    void save(Device device);

    Device findByAddress(String address);

    void bind(Device device);

    void update(Device device);

    IPage<DeviceVo> list(Page<DeviceVo> page, @Param("queryParams") DeviceSearchDto queryParams);

    List<Device> findByOwnerAddress(String ownerAddress);

    void terminate(DeviceTerminateMinerDto deviceTerminateMinerDto);

    DeviceVo queryByMiner(@Param("minerAddress")String minerAddress);
}
