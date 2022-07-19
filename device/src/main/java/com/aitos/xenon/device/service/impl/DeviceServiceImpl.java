package com.aitos.xenon.device.service.impl;

import com.aitos.xenon.account.api.RemoteAccountService;
import com.aitos.xenon.account.api.RemoteTransactionService;
import com.aitos.xenon.account.api.domain.dto.AccountRegisterDto;
import com.aitos.xenon.account.api.domain.dto.TransactionDto;
import com.aitos.xenon.account.api.domain.vo.AccountVo;
import com.aitos.xenon.block.api.RemoteBlockService;
import com.aitos.xenon.common.crypto.ed25519.Base58;
import com.aitos.xenon.core.constant.ApiStatus;
import com.aitos.xenon.core.constant.BusinessConstants;
import com.aitos.xenon.core.exceptions.account.OwnerAccountNotExistException;
import com.aitos.xenon.core.exceptions.account.PayerAccountNotExistException;
import com.aitos.xenon.core.exceptions.device.DeviceExistedException;
import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.core.utils.BeanConvertor;
import com.aitos.xenon.device.api.domain.dto.DeviceRegisterDto;
import com.aitos.xenon.device.api.domain.dto.DeviceSearchDto;
import com.aitos.xenon.device.domain.Device;
import com.aitos.xenon.device.domain.DeviceDetial;
import com.aitos.xenon.device.api.domain.dto.DeviceBindDto;
import com.aitos.xenon.device.api.domain.vo.DeviceDetialVo;
import com.aitos.xenon.device.api.domain.vo.DeviceVo;
import com.aitos.xenon.device.mapper.DeviceMapper;
import com.aitos.xenon.device.service.DeviceDetialService;
import com.aitos.xenon.device.service.DeviceService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private DeviceDetialService deviceDetialService;

    @Autowired
    private RemoteAccountService remoteAccountService;

    @Autowired
    private RemoteTransactionService remoteTransactionService;

    @Autowired
    private RemoteBlockService remoteBlockService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(DeviceRegisterDto deviceRegister) {
        Device device= BeanConvertor.toBean(deviceRegister,Device.class);
        String address= Hex.encodeHexString(Base58.decode(deviceRegister.getAddress()));
        device.setAddress(address);

        deviceMapper.save(device);
        AccountRegisterDto accountRegisterDto =new AccountRegisterDto();
        accountRegisterDto.setAddress(device.getAddress());
        accountRegisterDto.setAccountType(BusinessConstants.AccountType.MINER);
        accountRegisterDto.setBalance(new BigDecimal("0"));
        accountRegisterDto.setNonce(0l);
        Result result=remoteAccountService.register(accountRegisterDto);
        if(result.getCode()!= ApiStatus.SUCCESS.getCode()){

           throw new DeviceExistedException("设备账户已经存在");
        }

        Result<Long> blockVoResult= remoteBlockService.getBlockHeight();
        //记录交易信息
        TransactionDto transactionDto=new TransactionDto();
        transactionDto.setFromAddress(device.getAddress());
        transactionDto.setHeight(blockVoResult.getData());
        transactionDto.setData(deviceRegister.getTxData());
        String txHash=Base58.encode(DigestUtils.sha256(deviceRegister.getTxData()));
        transactionDto.setHash(txHash);
        transactionDto.setTxType(BusinessConstants.TXType.TX_REGISTER_MINER);
        remoteTransactionService.transaction(transactionDto);
    }

    @Override
    public Device findByAddress(String address) {
        Device  device= deviceMapper.findByAddress(address);
        return device;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bind(DeviceBindDto deviceBindDto) {
        //判断payer的金额是否满足
        Result<AccountVo>  payerAccountVoResult= remoteAccountService.findByAddress(deviceBindDto.getPayerAddress());
        AccountVo payerAccountVo=payerAccountVoResult.getData();
        if(payerAccountVo==null){
            throw new PayerAccountNotExistException("付款方账户不存在");
        }
        /*if(payerAccountVo.getBalance().subtract(deviceBindDto.getStakingFee()).compareTo(new BigInteger("0"))>0){
            throw new PayerAccountNotEnoughException("账户额度不够");
        }*/

        Result<AccountVo>  ownerAccountVoResult= remoteAccountService.findByAddress(deviceBindDto.getOwnerAddress());
        long accountId=0l;
        if(ownerAccountVoResult.getData()==null){
            AccountRegisterDto accountRegisterDto=new AccountRegisterDto();
            accountRegisterDto.setAddress(deviceBindDto.getOwnerAddress());
            accountRegisterDto.setAccountType(BusinessConstants.AccountType.MINER);
            Result<Long> registerResult=remoteAccountService.register(accountRegisterDto);
            if(registerResult.getCode()!=ApiStatus.SUCCESS.getCode()){
                throw new OwnerAccountNotExistException("owner账户创建失败");
            }else{
                accountId=registerResult.getData();
            }
        }else{
            AccountVo accountVo=ownerAccountVoResult.getData();
            accountId=accountVo.getId();
        }

        Device device=new Device();
        device.setId(deviceBindDto.getDeviceId());
        device.setAddress(deviceBindDto.getMinerAddress());
        device.setAccountId(accountId);
        if(deviceBindDto.getLocation()!=null){
            device.setLocationType(deviceBindDto.getLocation().getLocationType());
            device.setLatitude(deviceBindDto.getLocation().getLatitude());
            device.setLongitude(deviceBindDto.getLocation().getLongitude());
            device.setH3index(deviceBindDto.getLocation().getH3index());
        }
        deviceMapper.update(device);

        DeviceDetial deviceDetial= BeanConvertor.toBean(deviceBindDto.getMinerInfo(),DeviceDetial.class);
        deviceDetial.setDeviceId(deviceBindDto.getDeviceId());
        /*deviceDetial.setLocationType(deviceBindDto.getMinerInfo().getLocation().getLocationType());
        deviceDetial.setLatitude(deviceBindDto.getMinerInfo().getLocation().getLatitude());
        deviceDetial.setLongitude(deviceBindDto.getMinerInfo().getLocation().getLongitude());
        deviceDetial.setH3index(deviceBindDto.getMinerInfo().getLocation().getH3index());*/

        deviceDetialService.save(deviceDetial);

        Result<Long> blockVoResult= remoteBlockService.getBlockHeight();
        //记录交易信息
        TransactionDto transactionDto=new TransactionDto();
        transactionDto.setFromAddress(device.getAddress());
        transactionDto.setHeight(blockVoResult.getData());
        transactionDto.setData(deviceBindDto.getTxData());
        String txHash=Base58.encode(DigestUtils.sha256(deviceBindDto.getTxData()));
        transactionDto.setHash(txHash);
        transactionDto.setTxType(BusinessConstants.TXType.TX_ONBOARD_MINER);
        remoteTransactionService.transaction(transactionDto);
    }

    @Override
    public IPage<Device> list(DeviceSearchDto queryParams) {
        Page<Device> page=new Page<Device>(queryParams.getOffset(),queryParams.getLimit());
        IPage<Device> pageResult=deviceMapper.list(page,queryParams);
        return pageResult;
    }

    @Override
    public List<DeviceVo> queryByOwner(String ownerAddress) {
        Result<AccountVo> accountVoResult= remoteAccountService.findByAddress(ownerAddress);
        if(accountVoResult.getCode()!=ApiStatus.SUCCESS.getCode()){
            return null;
        }
        List<DeviceVo> list=deviceMapper.queryByOwner(accountVoResult.getData().getId());
        list.forEach(item->{
            DeviceDetial  deviceDetial=deviceDetialService.findByDeviceId(item.getId());
            DeviceDetialVo deviceDetialVo=BeanConvertor.toBean(deviceDetial,DeviceDetialVo.class);
            item.setDeviceDetial(deviceDetialVo);
        });
        return list;
    }

    @Override
    public DeviceVo queryByMiner(String minerAddress) {
        Device device=deviceMapper.findByAddress(minerAddress);
        DeviceVo deviceVo=BeanConvertor.toBean(device,DeviceVo.class);
        if(device!=null){
            DeviceDetial  deviceDetial=deviceDetialService.findByDeviceId(device.getId());
            DeviceDetialVo deviceDetialVo=BeanConvertor.toBean(deviceDetial,DeviceDetialVo.class);
            deviceVo.setDeviceDetial(deviceDetialVo);
        }
        return deviceVo;
    }

    @Override
    public DeviceDetial findDetialByDeviceId(Long deviceId) {
        return deviceDetialService.findByDeviceId(deviceId);
    }


}
