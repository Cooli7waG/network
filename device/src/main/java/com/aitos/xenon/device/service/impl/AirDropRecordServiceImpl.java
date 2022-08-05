package com.aitos.xenon.device.service.impl;

import com.aitos.xenon.account.api.RemoteAccountService;
import com.aitos.xenon.account.api.RemoteTransactionService;
import com.aitos.xenon.account.api.domain.dto.AccountRegisterDto;
import com.aitos.xenon.account.api.domain.dto.TransactionDto;
import com.aitos.xenon.account.api.domain.vo.AccountVo;
import com.aitos.xenon.block.api.RemoteBlockService;
import com.aitos.xenon.core.constant.ApiStatus;
import com.aitos.xenon.core.constant.BusinessConstants;
import com.aitos.xenon.core.exceptions.ServiceException;
import com.aitos.xenon.core.exceptions.account.OwnerAccountNotExistException;
import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.core.utils.BeanConvertor;
import com.aitos.xenon.device.api.domain.dto.AirDropDto;
import com.aitos.xenon.device.api.domain.dto.ClaimDto;
import com.aitos.xenon.device.api.domain.dto.DeviceInfoDto;
import com.aitos.xenon.device.api.domain.dto.DeviceLocationDto;
import com.aitos.xenon.device.domain.AirDropRecord;
import com.aitos.xenon.device.domain.Device;
import com.aitos.xenon.device.mapper.AirDropRecordMapper;
import com.aitos.xenon.device.mapper.DeviceMapper;
import com.aitos.xenon.device.service.AirDropRecordService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
public class AirDropRecordServiceImpl implements AirDropRecordService {

    @Autowired
    private AirDropRecordMapper airDropRecordMapper;

    @Autowired
    private RemoteBlockService blockService;

    @Autowired
    private RemoteTransactionService remoteTransactionService;

    @Autowired
    private RemoteBlockService remoteBlockService;

    @Autowired
    private RemoteAccountService remoteAccountService;

    @Autowired
    private DeviceMapper deviceMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(AirDropDto airDropDto) {
        AirDropRecord  airDropRecord= BeanConvertor.toBean(airDropDto,AirDropRecord.class);
        airDropRecord.setCreateTime(LocalDateTime.now());

        DeviceLocationDto locationDto = airDropDto.getLocation();
        airDropRecord.setLocationType(locationDto.getLocationType());
        airDropRecord.setLongitude(locationDto.getLongitude());
        airDropRecord.setLatitude(locationDto.getLatitude());
        airDropRecord.setH3index(locationDto.getH3index());

        DeviceInfoDto minerInfo = airDropDto.getMinerInfo();
        airDropRecord.setPower(minerInfo.getPower());
        airDropRecord.setCapabilities(minerInfo.getCapabilities());
        airDropRecord.setEnergy(minerInfo.getEnergy());
        airDropRecord.setDeviceModel(minerInfo.getDeviceModel());
        airDropRecord.setDeviceSerialNum(minerInfo.getDeviceSerialNum());
        airDropRecord.setStatus(BusinessConstants.DeviceAirdropStatus.NOT_CLAIM);

        Result<Long> blockHeightResult = blockService.getBlockHeight();
        if(blockHeightResult.getCode()!= ApiStatus.SUCCESS.getCode()){
            throw new ServiceException(blockHeightResult.getMsg());
        }
        //3天
        airDropRecord.setExpiration(blockHeightResult.getData()+60*24*3);
        airDropRecordMapper.save(airDropRecord);


        Result<Long> blockVoResult= remoteBlockService.getBlockHeight();
        if(blockVoResult.getCode()!= ApiStatus.SUCCESS.getCode()){
            throw new ServiceException(blockVoResult.getMsg());
        }
        String txData= JSON.toJSONString(airDropDto);
        String txHash= DigestUtils.sha256Hex(txData);
        //记录交易信息
        TransactionDto transactionDto=new TransactionDto();
        transactionDto.setFromAddress(airDropDto.getMinerAddress());
        transactionDto.setHeight(blockVoResult.getData());
        transactionDto.setData(txData);

        transactionDto.setHash(txHash);
        transactionDto.setTxType(BusinessConstants.TXType.TX_AIRDROP_MINER);
        Result<String> transaction = remoteTransactionService.transaction(transactionDto);
        if(transaction.getCode()!= ApiStatus.SUCCESS.getCode()){
            throw new ServiceException(transaction.getMsg());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void claim(ClaimDto claimDto) {
        airDropRecordMapper.claim(claimDto);

        Result<AccountVo>  ownerAccountVoResult= remoteAccountService.findByAddress(claimDto.getOwnerAddress());
        log.info("bind.ownerAccountVoResult=", JSON.toJSONString(ownerAccountVoResult));
        if(ownerAccountVoResult.getData()==null){
            AccountRegisterDto accountRegisterDto=new AccountRegisterDto();
            accountRegisterDto.setAddress(claimDto.getOwnerAddress());
            accountRegisterDto.setAccountType(BusinessConstants.AccountType.WALLET);
            Result<Long> registerResult=remoteAccountService.register(accountRegisterDto);
            log.info("bind.registerResult=", JSON.toJSONString(registerResult));
            if(registerResult.getCode()!=ApiStatus.SUCCESS.getCode()){
                throw new OwnerAccountNotExistException("owner账户创建失败");
            }
        }

        AirDropRecord airDropRecord = findByMinerAddress(claimDto.getMinerAddress());

        Device device=new Device();
        device.setAddress(airDropRecord.getMinerAddress());
        device.setOwnerAddress(airDropRecord.getOwnerAddress());

        device.setLocationType(airDropRecord.getLocationType());
        device.setLatitude(airDropRecord.getLatitude());
        device.setLongitude(airDropRecord.getLongitude());
        device.setH3index(airDropRecord.getH3index());

        device.setEnergy(airDropRecord.getEnergy());
        device.setCapabilities(airDropRecord.getCapabilities());
        device.setPower(airDropRecord.getPower());
        device.setDeviceModel(airDropRecord.getDeviceModel());
        device.setDeviceSerialNum(airDropRecord.getDeviceSerialNum());
        device.setUpdateTime(LocalDateTime.now());
        deviceMapper.bind(device);

        Result<Long> blockVoResult= remoteBlockService.getBlockHeight();
        if(blockVoResult.getCode()!= ApiStatus.SUCCESS.getCode()){
            throw new ServiceException(blockVoResult.getMsg());
        }
        //记录交易信息
        String txData= JSON.toJSONString(claimDto);
        String txHash= DigestUtils.sha256Hex(txData);

        TransactionDto transactionDto=new TransactionDto();
        transactionDto.setFromAddress(device.getAddress());
        transactionDto.setHeight(blockVoResult.getData());
        transactionDto.setData(txData);

        transactionDto.setHash(txHash);
        transactionDto.setTxType(BusinessConstants.TXType.TX_CLAIM_MINER);
        Result<String> transaction = remoteTransactionService.transaction(transactionDto);
        if(transaction.getCode()!= ApiStatus.SUCCESS.getCode()){
            throw new ServiceException(transaction.getMsg());
        }


    }

    @Override
    public AirDropRecord findNotClaimedByMinerAddress(String minerAddress) {
        return airDropRecordMapper.findNotClaimedByMinerAddress(minerAddress);
    }

    @Override
    public AirDropRecord findByMinerAddress(String minerAddress) {
        return airDropRecordMapper.findByMinerAddress(minerAddress);
    }
}
