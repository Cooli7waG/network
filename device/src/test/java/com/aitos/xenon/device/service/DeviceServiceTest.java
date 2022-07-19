package com.aitos.xenon.device.service;

import com.aitos.xenon.core.constant.BusinessConstants;
import com.aitos.xenon.device.api.domain.dto.DeviceRegisterDto;
import com.aitos.xenon.device.domain.Device;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class DeviceServiceTest {

    @Autowired
    private DeviceService  deviceService;

    @Test
    public void test_save(){
        DeviceRegisterDto device=new DeviceRegisterDto();
        device.setAddress("100000000000000000000000000000001");
        device.setMinerType(BusinessConstants.DeviceMinerType.VIRTUAL_MINER);
        device.setMaker("test");
        deviceService.save(device);
    }

    @Test
    public void test_json(){

        String json="{\"version\":1,\"minerAddress\":\"GfHq2tTVk9z4eXgyN19dJf6qN5UuoQe8Kxm22TaLrTRXKXESDDCHVAxk3gbN\",\"ownerAddress\":\"GfHq2tTVk9z4eXgyHGZj8SQVRhkmERPXPUUDixb6KyAziRouLAsLJ7cWNV1V\",\"payerAddress\":\"GfHq2tTVk9z4eXgyHGZj8SQVRhkmERPXPUUDixb6KyAziRouLAsLJ7cWNV1V\",\"locationType\":0,\"latitude\":130.55555,\"longitude\":130.55555,\"h3index\":1,\"minerInfo\":{\"version\":1,\"startBlock\":1,\"latitude\":130.55555,\"longitude\":130.55555,\"h3index\":1,\"energy\":0,\"capabilities\":\"设备能力\",\"deviceModel\":\"设备型号\",\"deviceSerialNum\":\"设备序列号\",\"sGVoltage\":1,\"sGCurrent\":1,\"sGPowerLow\":1},\"minerSignature\":\"L2JQet6jSgwRpZ7dZCQcYFswteZdPPkhwJU5MWcM1xAXocDhVpzgFv3r12scabkuMUk3f1QbQh6RoSU9QHQZ4iY\",\"ownerSignature\":\"4sbBrcWvUtfgCdzVLvUnE6SoSzRqCq79TVJ6ZmSdqKU4zaboD6a2BBhYEJuwwmnNV6yGoW1pqEEMJvzd2AYJ1pnG\",\"payerSignature\":\"45pBiyHgghwy4pFY5f1RjX2JXhCGgACXCkYhPoXkaS3T86YcwCT4PS6S7jJAtw65apU6Ape16jTYqGkB7f7JDDF3\"}";
        JSONObject params = JSONObject.parseObject(json, Feature.OrderedField);
        System.out.println(params.toJSONString());
        params.remove("payerSignature");
        System.out.println(params.toJSONString());
    }
}
