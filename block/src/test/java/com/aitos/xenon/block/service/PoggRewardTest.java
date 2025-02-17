package com.aitos.xenon.block.service;

import com.aitos.xenon.block.domain.*;
import com.aitos.xenon.block.mapper.PoggMapper;
import com.aitos.xenon.block.mapper.PoggRewardMapper;
import com.aitos.xenon.core.constant.BusinessConstants;
import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class PoggRewardTest {

    @Autowired
    private BlockService blockService;

    @Autowired
    private PoggMapper poggMapper;

    @Autowired
    private PoggReportService poggReportService;

    @Autowired
    private SystemConfigService systemConfigService;


    @Test
    public void test_RewardCalculation(){
        Block block= blockService.getCurrentBlock();
        System.out.println(JSON.toJSONString(block));
        //查询已结束的commit记录
        PoggCommit poggCommit=poggMapper.findOverCommit();
        if(poggCommit==null){
            System.out.println("查询已结束的commit记录==0");
            return;
        }
        SystemConfig systemConfig = systemConfigService.findConfig(false);

        //统计自定范围内miner上报的记录数
        long startEpoch=poggCommit.getEpoch()-systemConfig.getCalDataRange();
        long endEpoch=poggCommit.getEpoch();
        System.out.println(JSON.toJSONString(systemConfig));
        System.out.println("startEpoch:"+startEpoch);
        System.out.println("endEpoch:"+endEpoch);
        /**
         * 查询统计出每个miner中所有epoch的记录条数(单个epoch最高12条)，
         * 并且过滤了当前epoch里没有记录的miner
         */
        List<PoggReportSubtotalStatistics> subtotalStatisticsList = poggReportService.findSubtotalStatisticsList(startEpoch, endEpoch);
        if(subtotalStatisticsList.size()==0){
            System.out.println("subtotalStatisticsList.size()==0");
            return;
        }
        System.out.println("查询统计出每个miner中所有epoch的记录条数:");
        for (PoggReportSubtotalStatistics poggReportSubtotalStatistics : subtotalStatisticsList) {
            System.out.println(JSON.toJSONString(poggReportSubtotalStatistics));
        }
        System.out.println("------------------------------------------------");
        //获得有资格的miner
        List<PoggReportSubtotalStatistics> qualifiedMinerList = subtotalStatisticsList.parallelStream()
                .filter(item ->  processAwardEligibility(poggCommit.getPrivateKey(), item.getAddress(), item.getTotalRecord()))
                .collect(Collectors.toList());
        if(qualifiedMinerList.size()==0){
            System.out.println("qualifiedMinerList.size()==0");
            return;
        }
        System.out.println("获得有资格的miner:");
        for (PoggReportSubtotalStatistics poggReportSubtotalStatistics : qualifiedMinerList) {
            System.out.println(JSON.toJSONString(poggReportSubtotalStatistics));
        }
        System.out.println("------------------------");

        //计算总的奖励权重
        BigDecimal totalRewardWeight=calTotalRewardWeight(systemConfig,qualifiedMinerList);
        System.out.println("totalRewardWeight:"+totalRewardWeight);

        //计算每个miner获得的奖励
        List<PoggRewardDetail> rewards=subtotalStatisticsList.stream().map(item->{
            BigDecimal awardNumber = rewardCalculation(systemConfig,totalRewardWeight, item.getMinerType(), item.getTotalRecord());
            PoggRewardDetail poggRewardDetail=new PoggRewardDetail();
            poggRewardDetail.setAddress(item.getAddress());
            poggRewardDetail.setOwnerAddress(item.getOwnerAddress());
            poggRewardDetail.setAmount(awardNumber);
            return  poggRewardDetail;
        }).collect(Collectors.toList());

        //封装数据，并存入到数据库中
        PoggReward poggReward=new PoggReward();
        poggReward.setHeight(block.getHeight());
        poggReward.setVerifiableEvidence(poggCommit.getPrivateKey());
        poggReward.setStartEpoch(startEpoch);
        poggReward.setEndEpoch(endEpoch);
        poggReward.setStatus(BusinessConstants.POGGRewardStatus.UN_ISSUED);
        poggReward.setRewardsJson(JSON.toJSONString(rewards));
        poggReward.setCreateTime(LocalDateTime.now());
        //poggRewardMapper.save(poggReward);

        //将已结束的状态修改为奖励已计算
        PoggCommit calculatedPoggCommit=new PoggCommit();
        calculatedPoggCommit.setId(poggCommit.getId());
        calculatedPoggCommit.setStatus(BusinessConstants.POGGCommitStatus.REWARD_CALCULATED);
        calculatedPoggCommit.setUpdateTime(LocalDateTime.now());
        //poggMapper.updateCommit(calculatedPoggCommit);
    }

    public boolean processAwardEligibility(String commitPrivatekey, String minerAddress,  Integer minerRecordTotal){

        SystemConfig systemConfig = systemConfigService.findConfig(false);

        //奖励资格获得1次奖励
        int perRewardEligibilityBlocks=systemConfig.getPerRewardBlocks();

        byte[] commitPrivateKeyBytes= Hex.decode(commitPrivatekey);
        byte[] minerAddressByte=Hex.decode(minerAddress);
        byte[] ecdhBytes=new byte[commitPrivateKeyBytes.length+minerAddressByte.length];
        System.arraycopy(commitPrivateKeyBytes, 0, ecdhBytes, 0, commitPrivateKeyBytes.length);
        System.arraycopy(minerAddressByte, 0, ecdhBytes, commitPrivateKeyBytes.length-1, minerAddressByte.length);

        /*String privateKey="3hupFSQNWwiJuQNc68HiWzPgyNpQA2yy9iiwhytMS7rZyfCJDNrSLBqS8QguVBgam5TLWqgRFwSME86GUHpJrfGxqzgQLGB99cmU8FxzvWEg3WTGUTuCrp9XuRyJ5Sdej62WzJSVcr6Mmj9utPApB4VsqWMY4Z74v8xQx78t8wQmTR2FeBeurwAPzeJuMWB72xzA9";
        String publickKey="PZ8Tyr4Nx8MHsRAGMpZmZ6TWY63dXWSCyYX7kae74h2wDin6wmJwpbMqGUrKxMf2FQA3nw616bhpmXKrEEQ5A3KkcY793AsKpF7EA5Rf1Yq1scnPAXunZEQd";
        ecdhBytes=XenonCrypto.doECDH(privateKey,publickKey);*/

        BigInteger x= new BigInteger(ecdhBytes).multiply(new BigInteger(minerRecordTotal.toString()));
        System.out.println("x="+x);
        byte[] sha256Bytes= DigestUtils.sha256(x.toByteArray());
        System.out.println("sha256Bytes="+new BigInteger(sha256Bytes));

        BigInteger seed=new BigInteger(sha256Bytes).remainder(new BigInteger(perRewardEligibilityBlocks+""));

        System.out.println(seed);
        return seed.compareTo(new BigInteger("0"))!=0;
    }

    /**
     * 计算总的奖励权重
     */
    public BigDecimal calTotalRewardWeight(SystemConfig systemConfig,List<PoggReportSubtotalStatistics> qualifiedMinerList){
        BigDecimal totalRewardWeight = qualifiedMinerList.stream()
                .map(item->BigDecimal.valueOf(calCoefficient(systemConfig,item.getMinerType())*item.getTotalRecord())).reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        return totalRewardWeight;
    }

    /**
     * 计算奖励分配
     * @param minerRecordTotal
     */
    public BigDecimal rewardCalculation(SystemConfig systemConfig,BigDecimal totalRewardWeight,Integer minerType,Integer minerRecordTotal){

        //系数
        float  coefficient=calCoefficient(systemConfig,minerType);

        //单个miner 的奖励权重
        BigDecimal minerRewardWeight = BigDecimal.valueOf(coefficient * minerRecordTotal);

        //每个miner奖励比例
        BigDecimal minerRewardRatio = minerRewardWeight.divide(totalRewardWeight,8, RoundingMode.HALF_UP);


        //每个epoch奖励token数量
        BigDecimal token=systemConfig.getPerEpochTokenNumber();

        //奖励数量
        BigDecimal awardNumber = token.multiply(minerRewardRatio);

        return awardNumber;
    }

    /**
     * 计算系数
     * @return
     */
    private float calCoefficient(SystemConfig systemConfig,Integer minerType){
        if(minerType.equals(BusinessConstants.DeviceMinerType.GAME_MINER)){
            return systemConfig.getGameMinerCoefficient();
        }else if (minerType.equals(BusinessConstants.DeviceMinerType.API_MINER)){
            return systemConfig.getApiMinerCoefficient();
        }else if (minerType.equals(BusinessConstants.DeviceMinerType.STANDARD_PV_MINER)){
            return systemConfig.getStandardPvMinerCoefficient();
        }else if (minerType.equals(BusinessConstants.DeviceMinerType.LITE_PV_MINER)){
            return systemConfig.getLitePvMinerCoefficient();
        }else if (minerType.equals(BusinessConstants.DeviceMinerType.VIRTUAL_MINER)){
            return systemConfig.getVirtualMinerCoefficient();
        }
        return 0;
    }
}
