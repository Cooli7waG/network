package com.aitos.xenon.core.utils;

public class LocationTransformUtils {
    private static final double PI = Math.PI;
    private static final double mercatorMax = 20037508.34;

    /**
     * 4326坐标转3857即经纬度转墨卡托
     * @param longitude
     * @param latitude
     */
    public static Location transformTo3857(Double longitude,Double latitude){
        if(longitude==null || latitude==null){
            return new Location();
        }
        double mercatorx = longitude * mercatorMax / 180;
        double mercatory = Math.log(Math.tan(((90+latitude) * PI) / 360)) / (PI / 180);
        mercatory = mercatory * mercatory / 180;
        Location location = new Location();
        location.setLatitude(mercatory);
        location.setLongitude(mercatorx);
        return location;
    }

    /**
     * 墨卡托坐标转3857即墨卡托转经纬度
     * @param longitude
     * @param latitude
     */
    public static Location tarnsformTo4326(double longitude,double latitude){
        double lon = longitude/mercatorMax * 180;
        double lat = latitude/mercatorMax * 180;
        lat = (180 / PI) * (2 *Math.atan(Math.exp((lat * PI) / 180)) - PI / 2);
        Location location = new Location();
        location.setLatitude(lat);
        location.setLongitude(lon);
        return location;
    }
}
