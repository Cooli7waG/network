package com.aitos.xenon.block.service.impl;

import com.aitos.xenon.account.api.RemoteTransactionService;
import com.aitos.xenon.account.api.domain.dto.BatchRewardMinersDto;
import com.aitos.xenon.account.api.domain.dto.TransactionDto;
import com.aitos.xenon.block.api.domain.vo.PoggChallengeVo;
import com.aitos.xenon.block.domain.Block;
import com.aitos.xenon.block.domain.PoggChallenge;
import com.aitos.xenon.block.domain.PoggChallengeRecord;
import com.aitos.xenon.block.domain.PoggChallengeRecordCount;
import com.aitos.xenon.block.mapper.PoggMapper;
import com.aitos.xenon.block.service.BlockService;
import com.aitos.xenon.block.service.PoggService;
import com.aitos.xenon.common.crypto.ed25519.Base58;
import com.aitos.xenon.core.constant.ApiStatus;
import com.aitos.xenon.core.constant.BusinessConstants;
import com.aitos.xenon.core.exceptions.ServiceException;
import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.device.api.RemoteDeviceService;
import com.aitos.xenon.device.api.domain.dto.DeviceDetialDto;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PoggServiceImpl implements PoggService {

    @Autowired
    private BlockService blockService;

    @Autowired
    private PoggMapper poggMapper;

    @Autowired
    private RemoteTransactionService remoteTransactionService;

    @Autowired
    private RemoteDeviceService remoteDeviceService;

    @Value("${pogg.totalRewardBmt}")
    private BigDecimal totalRewardBmt;

    @Value("${pogg.timeoutRange}")
    private Integer timeoutRange;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void genChallenge() {
        Block block= blockService.getBlock();
        String random= UUID.randomUUID().toString().replaceAll("-","")+UUID.randomUUID().toString().replaceAll("-","");

        PoggChallenge pogg=new PoggChallenge();
        pogg.setRandom(random);
        pogg.setTimeout(block.getHeight()+timeoutRange);

        String txData=JSON.toJSONString(pogg);
        String txHash=DigestUtils.sha256Hex(txData);
        pogg.setTxHash(txHash);
        poggMapper.genChallenge(pogg);

        TransactionDto transactionDto =new TransactionDto();
        transactionDto.setHeight(block.getHeight());
        transactionDto.setData(txData);
        transactionDto.setHash(txHash);
        transactionDto.setTxType(BusinessConstants.TXType.TX_CHALLENGE_POGG);
        Result result=remoteTransactionService.transaction(transactionDto);
        if(result.getCode()!= ApiStatus.SUCCESS.getCode()){
            throw new ServiceException(result.getMsg());
        }
    }


    @Override
    public List<PoggChallenge> activeChallenges() {
        Block block=blockService.getBlock();
        List<PoggChallenge> poggChallengeList=poggMapper.activeChallenges(block.getHeight());
        return poggChallengeList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveChallengeRecord(PoggChallengeRecord poggChallengeRecord) {
        Block block= blockService.getBlock();

        poggMapper.saveChallengeRecord(poggChallengeRecord);

        TransactionDto transactionDto =new TransactionDto();
        transactionDto.setFromAddress(poggChallengeRecord.getAddress());
        transactionDto.setHeight(block.getHeight());
        String txHash=DigestUtils.sha256Hex(poggChallengeRecord.getData());
        transactionDto.setData(poggChallengeRecord.getData());
        transactionDto.setHash(txHash);
        transactionDto.setTxType(BusinessConstants.TXType.TX_RESPONSE_POGG);
        Result result=remoteTransactionService.transaction(transactionDto);
        if(result.getCode()!= ApiStatus.SUCCESS.getCode()){
            throw new ServiceException(result.getMsg());
        }
        DeviceDetialDto deviceDetialDto=JSON.parseObject(poggChallengeRecord.getData(),DeviceDetialDto.class);
        deviceDetialDto.setAddress(poggChallengeRecord.getAddress());
        Result deviceResult=remoteDeviceService.updateDeviceDetial(deviceDetialDto);
        if(deviceResult.getCode()!= ApiStatus.SUCCESS.getCode()){
            throw new ServiceException(result.getMsg());
        }
        return  txHash;
    }

    @Override
    public PoggChallengeRecord findChallengeRecordByRandom(String address, String random) {
        return poggMapper.findChallengeRecordByRandom(address,random);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reward() {
        //计算每个设备获得奖励数
        List<PoggChallengeRecordCount> list=poggMapper.findNoRewardChallengeList();
        if(list.size()==0){
            return;
        }

        List<Long> idList=new ArrayList<>();

        int totalCount=0;

        for(PoggChallengeRecordCount poggChallengeRecordCount:list){
            totalCount+=poggChallengeRecordCount.getCount();

            List<Long> subIdList = Arrays.stream(poggChallengeRecordCount.getIds().split(","))
                    .map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            idList.addAll(subIdList);
        }

        //计算每个账户的奖励
        Block block= blockService.getBlock();
        BigDecimal price=totalRewardBmt.divide(new BigDecimal(totalCount),18, RoundingMode.HALF_UP);
        List<BatchRewardMinersDto> minerRewardList=new ArrayList<>();
        for(PoggChallengeRecordCount poggChallengeRecordCount:list){
            BatchRewardMinersDto  batchRewardMinersDto=new BatchRewardMinersDto();
            batchRewardMinersDto.setAccountId(poggChallengeRecordCount.getAccountId());

            BigDecimal reward=price.multiply(new BigDecimal(poggChallengeRecordCount.getCount()));
            batchRewardMinersDto.setReward(reward);
            batchRewardMinersDto.setHeight(block.getHeight());
            minerRewardList.add(batchRewardMinersDto);
        }

        //调用奖励服务
        Result result=remoteTransactionService.batchRewardMiners(minerRewardList);
        log.info("batchRewardMiners.result:{}", JSON.toJSONString(result));

        //更新记录状态
        poggMapper.updatePoggChallengeRecordStatus(idList);
    }

    @Override
    public PoggChallenge queryChallenges(String txHash) {
        return poggMapper.queryChallenges(txHash);
    }
}
