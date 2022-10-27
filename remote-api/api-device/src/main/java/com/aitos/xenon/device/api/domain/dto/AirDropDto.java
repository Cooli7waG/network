package com.aitos.xenon.device.api.domain.dto;
import lombok.Data;

/**
 * @author hnngm
 */
@Data
public class AirDropDto {
    private int version;

    private String minerAddress;

    private String ownerAddress;

    private String email;

    private DeviceLocationDto location;

    private DeviceInfoDto minerInfo;
    /**
     * 基金会签名
     */
    private String signature;

    private Long expiration;
}
