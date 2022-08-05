package com.aitos.xenon.block.service;

import com.aitos.xenon.block.api.domain.dto.PoggGreenDataDto;
import com.aitos.xenon.block.api.domain.dto.PoggReportDto;
import com.aitos.xenon.block.domain.PoggReportSubtotal;
import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;


@SpringBootTest
public class PoggReportServiceTest {


    @Autowired
    public  PoggReportService  poggReportService;

    @Test
    public void test_green_data_convertor(){
        PoggReportDto poggReportDto=new PoggReportDto();


        List<String> greenDataList=new ArrayList<>();
        greenDataList.add("010062df65b90000000000640000000000002710");
        List<PoggGreenDataDto> list = PoggGreenDataDto.decode(greenDataList);

        poggReportDto.setAddress("dsafasfasfas");

        poggReportDto.setRawDataJSON(JSON.toJSONString(poggReportDto));
        poggReportDto.setGreenDataList(list);
        poggReportService.reportSave(poggReportDto);
    }

    @Test
    public void test_saveOrUpdate(){
        PoggReportSubtotal poggReportSubtotal=new PoggReportSubtotal();
        poggReportSubtotal.setAddress("fdasfas33");
        poggReportSubtotal.setRecordNum(18);
        poggReportSubtotal.setEpoch(1l);

        poggReportService.saveOrUpdate(poggReportSubtotal);
    }

}
