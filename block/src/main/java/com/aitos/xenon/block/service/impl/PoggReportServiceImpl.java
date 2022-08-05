package com.aitos.xenon.block.service.impl;

import com.aitos.xenon.account.api.RemoteTransactionService;
import com.aitos.xenon.account.api.domain.dto.TransactionDto;
import com.aitos.xenon.block.api.domain.dto.PoggGreenDataDto;
import com.aitos.xenon.block.api.domain.dto.PoggReportDto;
import com.aitos.xenon.block.domain.Block;
import com.aitos.xenon.block.domain.PoggReportSubtotal;
import com.aitos.xenon.block.domain.PoggReportSubtotalStatistics;
import com.aitos.xenon.block.mapper.PoggReportMapper;
import com.aitos.xenon.block.service.BlockService;
import com.aitos.xenon.block.service.PoggReportService;
import com.aitos.xenon.core.constant.ApiStatus;
import com.aitos.xenon.core.constant.BusinessConstants;
import com.aitos.xenon.core.exceptions.ServiceException;
import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.device.api.RemoteDeviceService;
import com.aitos.xenon.device.api.domain.dto.DeviceDto;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class PoggReportServiceImpl implements PoggReportService {

    public static final int RECORD_NUM = 12;
    @Autowired
    private PoggReportMapper poggReportMapper;
    @Autowired
    private RemoteTransactionService remoteTransactionService;


    @Autowired
    private BlockService blockService;

    @Autowired
    private RemoteDeviceService deviceService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String reportSave(PoggReportDto poggReportDto) {
        Block currentBlock = blockService.getCurrentBlock();
        poggReportMapper.batchSave(poggReportDto);

        //更新设备数据
        DeviceDto deviceDto=new DeviceDto();
        deviceDto.setAddress(poggReportDto.getAddress());
        PoggGreenDataDto poggGreenDataDto=poggReportDto.getGreenDataList().get(poggReportDto.getGreenDataList().size()-1);
        deviceDto.setTotalEnergyGeneration(poggGreenDataDto.getTotal());
        Result updateResult = deviceService.update(deviceDto);
        if(updateResult.getCode()!= ApiStatus.SUCCESS.getCode()){
            throw new ServiceException(updateResult.getMsg());
        }


        String txHash= DigestUtils.sha256Hex(poggReportDto.getRawDataJSON());
        TransactionDto transactionDto =new TransactionDto();
        transactionDto.setHeight(currentBlock.getHeight());
        transactionDto.setData(poggReportDto.getRawDataJSON());
        transactionDto.setHash(txHash);
        transactionDto.setTxType(BusinessConstants.TXType.TX_REPORT_POGG);
        Result result=remoteTransactionService.transaction(transactionDto);
        if(result.getCode()!= ApiStatus.SUCCESS.getCode()){
            throw new ServiceException(result.getMsg());
        }

        //统计上报记录数
        PoggReportSubtotal poggReportSubtotal=new PoggReportSubtotal();
        poggReportSubtotal.setAddress(poggReportDto.getAddress());
        poggReportSubtotal.setOwnerAddress(poggReportDto.getOwnerAddress());
        poggReportSubtotal.setMinerType(poggReportDto.getMinerType());
        poggReportSubtotal.setEpoch(poggReportDto.getEpoch());
        poggReportSubtotal.setRecordNum(poggReportDto.getGreenDataList().size());

        this.saveOrUpdate(poggReportSubtotal);

        return txHash;
    }


    @Override
    public void saveOrUpdate(PoggReportSubtotal poggReportSubtotal) {
        PoggReportSubtotal poggReportSubtotalTemp=poggReportMapper.findSubtotalByEpoch(poggReportSubtotal.getAddress(),poggReportSubtotal.getEpoch());
        if(poggReportSubtotalTemp==null){
            poggReportSubtotal.setRecordNum(poggReportSubtotal.getRecordNum()>12?12:poggReportSubtotal.getRecordNum());
            poggReportMapper.saveSubtotal(poggReportSubtotal);
        }else if(poggReportSubtotalTemp.getRecordNum()< RECORD_NUM){
            poggReportSubtotal.setId(poggReportSubtotalTemp.getId());
            int recordNum=poggReportSubtotalTemp.getRecordNum()+poggReportSubtotal.getRecordNum();
            poggReportSubtotal.setRecordNum(recordNum>RECORD_NUM?RECORD_NUM:recordNum);
            poggReportMapper.updateSubtotal(poggReportSubtotal);
        }
    }

    @Override
    public List<PoggReportSubtotalStatistics> findSubtotalStatisticsList(long startEpoch, long endEpoch) {
        return poggReportMapper.findSubtotalStatisticsList(startEpoch,endEpoch);
    }


}
