package com.aitos.xenon.device.mapper;

import com.aitos.xenon.core.model.QueryParams;
import com.aitos.xenon.device.api.domain.dto.DeviceSearchDto;
import com.aitos.xenon.device.api.domain.dto.DeviceTerminateMinerDto;
import com.aitos.xenon.device.domain.Device;
import com.aitos.xenon.device.api.domain.vo.DeviceVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeviceMapper {

    void save(Device device);

    Device findByAddress(String address);

    void bind(@Param("address") String minerAddressToHex,@Param("accountId") Long id);

    void update(Device device);

    IPage<Device> list(Page<Device> page, @Param("queryParams") DeviceSearchDto queryParams);

    List<Device> findByAccountId(Long accountId);

    List<DeviceVo> queryByOwner(Long accountId);

    void terminate(DeviceTerminateMinerDto deviceTerminateMinerDto);
}
