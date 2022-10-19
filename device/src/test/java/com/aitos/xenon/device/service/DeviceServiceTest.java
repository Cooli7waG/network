package com.aitos.xenon.device.service;

import com.aitos.xenon.core.constant.BusinessConstants;
import com.aitos.xenon.core.utils.BeanConvertor;
import com.aitos.xenon.device.api.domain.dto.DeviceInfoDto;
import com.aitos.xenon.device.api.domain.dto.DeviceRegisterDto;
import com.aitos.xenon.device.domain.DeviceDetial;
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
        device.setMinerType(BusinessConstants.DeviceMinerType.GAME_MINER);
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

    @Test
    public void test_json2(){

        String json="{\"version\":1,\"address\":\"Cf8z2Fo23hDp5vLwDVpZv4PtD8CDwE2CRCkei6gBgAoD\",\"random\":\"e93ede0a64544d09829d7cacf4e3d9ad02d54da368e8405ba8cc849a11b73f43\",\"randomSignature\":\"5wkhVYfdTFaT1eVLbB1LA88sfxb4XCYfAweuRGFmMGWs7ZB3SgDttFRoDGQAJ1erE84eART248qRs97aDCCgvzH9\",\"challengeHash\":\"bac67125c87e95d32a3c2dd42eb5f740cb9efb6441e5347c69204a66ffd3da9c\",\"todayChargeVol\":40.078431372549019,\"todayUsageVol\":66.313725490196077,\"intervalChargeVol\":59.921568627450981,\"intervalUsageVol\":56.666666666666664,\"signature\":\"37LPbYvBgR2czpfXyzMH2oPvb2BzCTxn1TkBMvSh1mEyNMzewqkSjXjMXgX4K6UaQZVtyZZQAx2aD6MMadegpJ27\"}";
        DeviceInfoDto params = JSONObject.parseObject(json, DeviceInfoDto.class);

        DeviceDetial  deviceDetial= BeanConvertor.toBean(params,DeviceDetial.class);
        System.out.println(deviceDetial);
    }
}
