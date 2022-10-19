package com.aitos.xenon.block.service.impl;

import com.aitos.common.crypto.ecdsa.Ecdsa;
import com.aitos.common.crypto.ecdsa.EcdsaKeyPair;
import com.aitos.xenon.account.api.RemoteIPFSService;
import com.aitos.xenon.account.api.RemoteTransactionService;
import com.aitos.xenon.account.api.domain.dto.*;
import com.aitos.xenon.block.api.domain.vo.BlockVo;
import com.aitos.xenon.block.domain.*;
import com.aitos.xenon.block.mapper.PoggMapper;
import com.aitos.xenon.block.mapper.PoggRewardMapper;
import com.aitos.xenon.block.service.*;
import com.aitos.xenon.core.constant.ApiStatus;
import com.aitos.xenon.core.constant.BusinessConstants;
import com.aitos.xenon.core.exceptions.ServiceException;
import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.core.utils.BeanConvertor;
import com.alibaba.fastjson.JSON;
import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
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
    private PoggReportService poggReportService;

    @Autowired
    private RemoteTransactionService remoteTransactionService;

    @Autowired
    private SystemConfigService systemConfigService;

    @Autowired
    private PoggRewardMapper poggRewardMapper;

    @Autowired
    private EpochRewardService epochRewardService;

    @Autowired
    private RemoteIPFSService remoteIPFSService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void commit() {
        Block block = blockService.getCurrentBlock();
        if (block == null) {
            return;
        }
        PoggCommit currentPoggCommit = findCurrentCommit();

        long epoch = currentPoggCommit != null ? currentPoggCommit.getEpoch()+1 : 0;

        PoggCommit poggCommit = new PoggCommit();

        EcdsaKeyPair ecdsaKeyPair = Ecdsa.genKeyPair();
        poggCommit.setPrivateKey(ecdsaKeyPair.getPrivateKeyHex());
        poggCommit.setPublicKey(ecdsaKeyPair.getAddressHex());
        poggCommit.setHeight(block.getHeight());
        poggCommit.setEpoch(epoch);
        poggCommit.setStatus(BusinessConstants.POGGCommitStatus.NOT_OVER);
        poggCommit.setCreateTime(LocalDateTime.now());
        poggMapper.saveCommit(poggCommit);

        if (currentPoggCommit != null) {
            //将上一个commit设置为已结束
            currentPoggCommit.setStatus(BusinessConstants.POGGCommitStatus.OVER);
            currentPoggCommit.setUpdateTime(LocalDateTime.now());
            poggMapper.updateCommit(currentPoggCommit);
        }

        String txData = JSON.toJSONString(poggCommit);
        String txHash = DigestUtils.sha256Hex(txData);

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setHeight(block.getHeight());
        transactionDto.setData(txData);
        transactionDto.setHash(txHash);
        transactionDto.setTxType(BusinessConstants.TXType.TX_COMMIT_POGG);
        //TODO
        Result result = remoteTransactionService.transaction(transactionDto);
        if (result.getCode() != ApiStatus.SUCCESS.getCode()) {
            throw new ServiceException(result.getMsg());
        }
    }

    @Override
    public PoggCommit findCurrentCommit() {
        return poggMapper.findCurrentCommit();
    }

    @Override
    public void blockDataToIpfs() {

        PoggCommit currentCommit = this.findCurrentCommit();

        PoggCommit prevPoggCommit = this.findByEpoch(currentCommit.getEpoch() - 1);

        List<BlockVo> listByHeight = blockService.findListByHeight(prevPoggCommit.getHeight(), currentCommit.getHeight());

        if(listByHeight.size()==0){
            return;
        }
        String blockRange=listByHeight.get(0).getHeight()+"-"+listByHeight.get(listByHeight.size()-1).getHeight();
        IPFSPutBlockDto iPFSPutBlockDto =new IPFSPutBlockDto();
        iPFSPutBlockDto.setBlockRange(blockRange);
        iPFSPutBlockDto.setDateTime(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(listByHeight.get(0).getCreateTime()));

        List<IPFSPutBlockDataDto> blockDataDtoList=BeanConvertor.toList(listByHeight,IPFSPutBlockDataDto.class);
        iPFSPutBlockDto.setData(blockDataDtoList);
        Result putBlockDataResult = remoteIPFSService.putBlockData(iPFSPutBlockDto);
        log.info("putBlockDataResult={}",JSON.toJSONString(putBlockDataResult));
    }
    @Override
    public PoggCommit findByEpoch(Long epoch) {
        return poggMapper.findByEpoch(epoch);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rewardCalculation() {
        Block block = blockService.getCurrentBlock();
        //查询已结束的commit记录
        PoggCommit poggCommit = poggMapper.findOverCommit();
        if (poggCommit == null) {
            return;
        }
        SystemConfig systemConfig = systemConfigService.findConfig(false);

        //统计自定范围内miner上报的记录数
        long startEpoch = poggCommit.getEpoch() - systemConfig.getCalDataRange();
        long endEpoch = poggCommit.getEpoch();

        EpochReward epochReward = epochRewardService.findByEpoch(poggCommit.getEpoch());
        if(epochReward==null){
            return;
        }
        systemConfig.setPerEpochTokenNumber(epochReward.getTokenNumber());

        String rewardsJson="[]";
        /**
         * 查询统计出每个miner中所有epoch的记录条数(单个epoch最高12条)，
         * 并且过滤了当前epoch里没有记录的miner
         */
        List<PoggReportSubtotalStatistics> subtotalStatisticsList = poggReportService.findSubtotalStatisticsList(startEpoch, endEpoch);
        if (subtotalStatisticsList.size() > 0){
            log.info("subtotalStatisticsList.size={}",subtotalStatisticsList.size());
            //获得有资格的miner
            List<PoggReportSubtotalStatistics> qualifiedMinerList = subtotalStatisticsList.parallelStream()
                    .filter(item -> processAwardEligibility(poggCommit.getPrivateKey(), item.getAddress(), item.getTotalRecord()))
                    .collect(Collectors.toList());
            log.info("qualifiedMinerList.size={}",qualifiedMinerList.size());
            if (qualifiedMinerList.size() > 0) {
                //计算总的奖励权重
                BigDecimal totalRewardWeight = calTotalRewardWeight(systemConfig, qualifiedMinerList);

                //计算每个miner获得的奖励
                List<PoggRewardDetail> rewards = qualifiedMinerList.stream().map(item -> {
                    BigDecimal awardNumber = rewardCalculation(systemConfig, totalRewardWeight, item.getMinerType(), item.getTotalRecord(),item.getEnergyGeneration());
                    PoggRewardDetail poggRewardDetail = new PoggRewardDetail();
                    poggRewardDetail.setAddress(item.getAddress());
                    poggRewardDetail.setOwnerAddress(item.getOwnerAddress());
                    poggRewardDetail.setAmount(awardNumber);
                    return poggRewardDetail;
                }).collect(Collectors.toList());

                rewardsJson = JSON.toJSONString(rewards);
            }
        }
        //封装数据，并存入到数据库中
        PoggReward poggReward = new PoggReward();
        poggReward.setHeight(block.getHeight());
        poggReward.setVerifiableEvidence(poggCommit.getPrivateKey());
        poggReward.setStartEpoch(startEpoch);
        poggReward.setEndEpoch(endEpoch);
        poggReward.setStatus(BusinessConstants.POGGRewardStatus.UN_ISSUED);
        poggReward.setRewardsJson(rewardsJson);
        poggReward.setCreateTime(LocalDateTime.now());
        poggRewardMapper.save(poggReward);

        //将已结束的状态修改为奖励已计算
        PoggCommit calculatedPoggCommit = new PoggCommit();
        calculatedPoggCommit.setId(poggCommit.getId());
        calculatedPoggCommit.setStatus(BusinessConstants.POGGCommitStatus.REWARD_CALCULATED);
        calculatedPoggCommit.setUpdateTime(LocalDateTime.now());
        poggMapper.updateCommit(calculatedPoggCommit);
    }

    /**
     * 计算miner是否有资格
     *
     * @param commitPrivatekey
     * @param minerAddress
     * @param minerRecordTotal miner统计窗口内合法采样数据条数
     * @return
     */
    public boolean processAwardEligibility(String commitPrivatekey, String minerAddress, Integer minerRecordTotal) {

        SystemConfig systemConfig = systemConfigService.findConfig(false);

        //奖励资格获得1次奖励
        int perRewardEligibilityBlocks = systemConfig.getPerRewardBlocks();

        byte[] commitPrivateKeyBytes = Hex.decode(commitPrivatekey.replaceAll("(?i)0x",""));
        byte[] minerAddressByte = Hex.decode(minerAddress.replaceAll("(?i)0x",""));
        byte[] ecdhBytes = new byte[commitPrivateKeyBytes.length + minerAddressByte.length];
        System.arraycopy(commitPrivateKeyBytes, 0, ecdhBytes, 0, commitPrivateKeyBytes.length);
        System.arraycopy(minerAddressByte, 0, ecdhBytes, commitPrivateKeyBytes.length - 1, minerAddressByte.length);

        /*String privateKey="3hupFSQNWwiJuQNc68HiWzPgyNpQA2yy9iiwhytMS7rZyfCJDNrSLBqS8QguVBgam5TLWqgRFwSME86GUHpJrfGxqzgQLGB99cmU8FxzvWEg3WTGUTuCrp9XuRyJ5Sdej62WzJSVcr6Mmj9utPApB4VsqWMY4Z74v8xQx78t8wQmTR2FeBeurwAPzeJuMWB72xzA9";
        String publickKey="PZ8Tyr4Nx8MHsRAGMpZmZ6TWY63dXWSCyYX7kae74h2wDin6wmJwpbMqGUrKxMf2FQA3nw616bhpmXKrEEQ5A3KkcY793AsKpF7EA5Rf1Yq1scnPAXunZEQd";
        ecdhBytes=XenonCrypto.doECDH(privateKey,publickKey);*/

        BigInteger x = new BigInteger(ecdhBytes).multiply(new BigInteger(minerRecordTotal.toString()));
        byte[] sha256Bytes = DigestUtils.sha256(x.toByteArray());

        BigInteger seed = new BigInteger(sha256Bytes).remainder(new BigInteger(perRewardEligibilityBlocks + ""));
        return seed.compareTo(new BigInteger("0")) == 0;
    }

    public static void main(String[] args) {
        System.out.println(Math.pow(1000000, 1.0/5));
    }
    /**
     * 计算系数
     *
     * @return
     */
    private double calCoefficient(SystemConfig systemConfig, Integer minerType,long energyGeneration) {
       return Math.pow(energyGeneration, 1.0/5);
    }

    /**
     * 计算总的奖励权重
     */
    public BigDecimal calTotalRewardWeight(SystemConfig systemConfig, List<PoggReportSubtotalStatistics> qualifiedMinerList) {
        BigDecimal totalRewardWeight = qualifiedMinerList.stream()
                .map(item -> BigDecimal.valueOf(calCoefficient(systemConfig, item.getMinerType(),item.getEnergyGeneration()) * item.getTotalRecord())).reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
        return totalRewardWeight;
    }

    /**
     * 计算奖励分配
     *
     * @param minerRecordTotal
     */
    public BigDecimal rewardCalculation(SystemConfig systemConfig, BigDecimal totalRewardWeight, Integer minerType, Integer minerRecordTotal, long energyGeneration) {

        //系数
        double coefficient = calCoefficient(systemConfig, minerType,energyGeneration);

        //单个miner 的奖励权重
        BigDecimal minerRewardWeight = BigDecimal.valueOf(coefficient * minerRecordTotal);

        //每个miner奖励比例
        BigDecimal minerRewardRatio = minerRewardWeight.divide(totalRewardWeight, 8, RoundingMode.HALF_UP);


        //每个epoch奖励token数量
        BigDecimal token = systemConfig.getPerEpochTokenNumber();

        //奖励数量
        BigDecimal awardNumber = token.multiply(minerRewardRatio);

        return awardNumber;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void giveOutRewards() {
        List<PoggReward> unIssuedPoggRewardList = poggRewardMapper.findListUnIssued();
        unIssuedPoggRewardList.forEach(item -> {
            int status = BusinessConstants.POGGRewardStatus.ISSUED;
            String msg = "";
            try{
                PoggRewardDto poggRewardDto = BeanConvertor.toBean(item, PoggRewardDto.class);
                List<PoggRewardDetailDto> poggRewardDetailDtos = JSON.parseArray(item.getRewardsJson(), PoggRewardDetailDto.class);
                poggRewardDto.setRewards(poggRewardDetailDtos);

                long startTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                Result<String> result = remoteTransactionService.poggReward(poggRewardDto);
                long endTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                log.info("giveOutRewards.result={},times={}", JSON.toJSONString(result),endTime-startTime);
                if (result.getCode() != ApiStatus.SUCCESS.getCode()) {
                    msg = result.getMsg();
                    status = BusinessConstants.POGGRewardStatus.ISSUED_FAILED;
                }
            }catch (RetryableException e){
                msg = "Read timed out";
                status = BusinessConstants.POGGRewardStatus.ISSUED_FAILED;
                log.error("giveOutRewards.id={},error={}",item.getId(),e);
            }catch (Exception e){
                log.error("giveOutRewards.id={},error={}",item.getId(),e);
            }
            poggRewardMapper.updateStatus(item.getId(), status, msg, LocalDateTime.now());
        });
    }
}
