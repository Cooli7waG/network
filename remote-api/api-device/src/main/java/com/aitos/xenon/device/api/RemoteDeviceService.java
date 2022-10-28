package com.aitos.xenon.device.api;

import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.core.utils.Location;
import com.aitos.xenon.device.api.domain.dto.DeviceDto;
import com.aitos.xenon.device.api.domain.dto.DeviceRegisterDto;
import com.aitos.xenon.device.api.domain.vo.DeviceVo;

import java.util.HashMap;

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

    @PostMapping("/device/register")
    Result register(@RequestBody DeviceRegisterDto deviceRegister);

    @PostMapping("/device/getMinerLocation")
    Result<HashMap<String, Location>> getMinerLocation();

    @PostMapping("/airdroprecord/airdrop")
    Result airdrop(@RequestBody String body);

    @PostMapping("/airdroprecord/claim")
    Result claim(@RequestBody String body);

}
