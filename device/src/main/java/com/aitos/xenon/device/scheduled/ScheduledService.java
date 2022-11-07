package com.aitos.xenon.device.scheduled;

import com.aitos.xenon.block.api.RemoteBlockService;
import com.aitos.xenon.block.api.RemotePoggService;
import com.aitos.xenon.block.api.domain.vo.PoggReportDataVo;
import com.aitos.xenon.core.constant.ApiStatus;
import com.aitos.xenon.core.constant.BusinessConstants;
import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.core.utils.SunRiseSetUtils;
import com.aitos.xenon.device.api.domain.vo.DeviceVo;
import com.aitos.xenon.device.service.DeviceService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

/**
 * 用于分析miner状态
 * @author xymoc
 */
@Component
@Slf4j
public class ScheduledService {

    @Autowired
    DeviceService deviceService;
    @Autowired
    private RemotePoggService poggService;
    /**
     * 分析miner状态
     * 每120分钟执行一次
     */
    //@Scheduled(cron = "0 0 0/2 * * *")
    @Scheduled(cron = "0 0 0/1 * * *")
    public void minerRunStatus(){
        List<DeviceVo> deviceVoList = deviceService.getAllMiner();
        for (DeviceVo deviceVo : deviceVoList) {
            if(deviceVo.getLongitude()==null||deviceVo.getLatitude()==null){
                log.error("Miner[{}] not have Location[null]",deviceVo.getAddress());
                updateMinerRunStatus(deviceVo, BusinessConstants.RunStatus.UNKNOWN);
                continue;
            }
            String longitude = deviceVo.getLongitude().toString();
            String latitude = deviceVo.getLatitude().toString();
            if(!StringUtils.hasText(longitude) || !StringUtils.hasText(latitude)){
                log.error("Miner[{}] not have Location[not null]",deviceVo.getAddress());
                updateMinerRunStatus(deviceVo, BusinessConstants.RunStatus.UNKNOWN);
                continue;
            }
            Date now = new Date();
            SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Result<PoggReportDataVo> poggReportDataVoResult = poggService.lastReport(deviceVo.getAddress());
            if(poggReportDataVoResult.getCode() == ApiStatus.SUCCESS.getCode()){
                try{
                    PoggReportDataVo data = poggReportDataVoResult.getData();
                    long nowMilli = now.getTime();
                    long reportMilli = data.getDeviceTime().toInstant(ZoneOffset.ofHours(0)).toEpochMilli();
                    //long reportMilli = data.getCreateTime().toInstant(ZoneOffset.ofHours(0)).toEpochMilli();
                    //
                    Date reportDate = new Date(reportMilli);
                    log.info("reportDate:{}   checkData:{}",sdfTime.format(reportDate),sdfTime.format(now));
                    //
                    if(nowMilli-reportMilli>1000*60*60*2){
                        // 超过2小时无数据
                        log.info("Miner[{}] ，超过2小时无数据， nowMilli:{}  reportMilli:{}，data:",deviceVo.getAddress(),nowMilli,reportMilli,JSON.toJSONString(data));
                        updateMinerRunStatus(deviceVo, BusinessConstants.RunStatus.ABNORMAL_NULL_DATA);
                    }else {
                        // 2小时内有数据
                        Date locationDate = SunRiseSetUtils.getLocationDate(longitude);
                        SunRiseSetUtils.SunRiseAndSunset sunRiseAndSunset = SunRiseSetUtils.getSunRiseAndSunset(latitude, longitude);
                        SimpleDateFormat sdf = new SimpleDateFormat("HH.mm");
                        String format = sdf.format(locationDate);
                        String sunrise = sunRiseAndSunset.getSunrise().replaceAll(":", ".");
                        String sunset = sunRiseAndSunset.getSunset().replaceAll(":", ".");
                        double z = Double.parseDouble(format);
                        double x = Double.parseDouble(sunrise);
                        double y = Double.parseDouble(sunset);
                        if(x<z && z<y){
                            long power = data.getPower();
                            if(power>0){
                                log.info("Miner[{}] 有光照，有数据，数据正常",deviceVo.getAddress());
                                updateMinerRunStatus(deviceVo, BusinessConstants.RunStatus.NORMAL);
                            }else {
                                log.error("Miner[{}] 有光照，有数据，数据不正常[power:{}]",deviceVo.getAddress(),data.getPower());
                                updateMinerRunStatus(deviceVo, BusinessConstants.RunStatus.ABNORMAL_EMPTY_DATA);
                            }
                        }else {
                            long power = data.getPower();
                            if(power>0){
                                log.error("Miner[{}] 无光照，有数据，数据不正常[power:{}] - nowMilli:{}  reportMilli:{}",power,deviceVo.getAddress(),nowMilli,reportMilli);
                                updateMinerRunStatus(deviceVo, BusinessConstants.RunStatus.ABNORMAL_DATA_ERROR);
                            }else {
                                updateMinerRunStatus(deviceVo, BusinessConstants.RunStatus.NORMAL);
                                log.error("Miner[{}] 无光照，有数据，nowMilli:{}  reportMilli:{}",deviceVo.getAddress(),nowMilli,reportMilli);
                            }
                        }
                    }
                }catch (Exception e){
                    updateMinerRunStatus(deviceVo, BusinessConstants.RunStatus.UNKNOWN_NOT_REPORT);
                }
            }else {
                log.info("Miner[{}] ,获取 lastReport 失败:{}",deviceVo.getAddress(),poggReportDataVoResult.getCode());
                updateMinerRunStatus(deviceVo, BusinessConstants.RunStatus.UNKNOWN_NOT_REPORT);
            }
        }
    }

    private void updateMinerRunStatus(DeviceVo deviceVo,int runStatus){
        if(deviceVo.getRunStatus() != runStatus){
            log.info("状态不一致，修改数据库");
            deviceService.updateMinerRunStatus(deviceVo.getId(), runStatus);
        }else {
            log.info("状态一致，跳过修改数据库");
        }

    }

}
