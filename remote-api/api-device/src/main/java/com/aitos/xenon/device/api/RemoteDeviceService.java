package com.aitos.xenon.device.api;


import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.device.api.domain.dto.DeviceDto;
import com.aitos.xenon.device.api.domain.vo.DeviceVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "device")
public interface RemoteDeviceService {

    @PostMapping("/device/onboard")
    Result<String> onboard(@RequestBody String params);

    @GetMapping("/device/queryByMiner")
    Result<DeviceVo> queryByMiner(@RequestParam("minerAddress") String minerAddress);

    @PutMapping("/device/update")
    Result update(@RequestBody DeviceDto deviceDto);
}
