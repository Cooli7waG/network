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
import com.aitos.xenon.core.utils.Location;
import com.aitos.xenon.device.api.RemoteDeviceService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.LongValue;

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
import java.util.Map.Entry;
import java.util.stream.Collector;
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
    private RemoteDeviceService remoteDeviceService;

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
                //计算每个 miner 的真实性得分
                List<PoggAuthenticity> authcs = qualifiedMinerList.stream().map(item -> {
                    return constructPoggAuthenticity(item.getAddress(), startEpoch, endEpoch);
                }).collect(Collectors.toList());
                Map<String, PoggAuthenticity> minerAuthcs = new HashMap<String, PoggAuthenticity>();
                authcs.forEach(item -> minerAuthcs.put(item.getAddress(), item));

                //计算总的奖励权重
                BigDecimal totalRewardWeight = calTotalRewardWeight(systemConfig, qualifiedMinerList, authcs);

                //计算每个miner获得的奖励
                List<PoggRewardDetail> rewards = qualifiedMinerList.stream().map(item -> {
                    BigDecimal awardNumber = rewardCalculation(systemConfig, totalRewardWeight,
                        item.getMinerType(), item.getTotalRecord(), item.getEnergyGeneration(),
                        minerAuthcs.get(item.getAddress()).getScore());
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
     * @param authcs
     */
    public BigDecimal calTotalRewardWeight(SystemConfig systemConfig, List<PoggReportSubtotalStatistics> qualifiedMinerList, List<PoggAuthenticity> authcs) {
        BigDecimal totalRewardWeight = BigDecimal.valueOf(0);
        for (int i = 0; i < qualifiedMinerList.size(); i++) {
            PoggReportSubtotalStatistics s = qualifiedMinerList.get(i);
            double c = calCoefficient(systemConfig, s.getMinerType(), s.getEnergyGeneration());
            double t = authcs.get(i).getScore();
            double z = s.getTotalRecord();
            totalRewardWeight.add(BigDecimal.valueOf(t * c * z));
        }
        return totalRewardWeight;
    }

    /**
     * 计算奖励分配
     *
     * @param minerRecordTotal
     */
    public BigDecimal rewardCalculation(SystemConfig systemConfig, BigDecimal totalRewardWeight, Integer minerType, Integer minerRecordTotal, long energyGeneration, double authcScore) {

        //系数
        double coefficient = calCoefficient(systemConfig, minerType,energyGeneration);

        //单个miner 的奖励权重
        BigDecimal minerRewardWeight = BigDecimal.valueOf(authcScore * coefficient * minerRecordTotal);

        //每个miner奖励比例
        BigDecimal minerRewardRatio = minerRewardWeight.divide(totalRewardWeight, 8, RoundingMode.HALF_UP);


        //每个epoch奖励token数量
        BigDecimal token = systemConfig.getPerEpochTokenNumber();

        //奖励数量
        BigDecimal awardNumber = token.multiply(minerRewardRatio);

        return awardNumber;
    }

    /**
     * 获取 miner 在 epoch 区间内的所有功率数据
     * @param endEpoch
     * @param startEpoch
     * @param address
     * @return
     */
    public List<PoggReportPowerData> getMinerPowerDataByEpoch(String address, long startEpoch, long endEpoch) {
        List<PoggReportPowerData> poggReportPowerDataList = poggReportService.findPowerDataListByEpoch(address, startEpoch, endEpoch);
        log.debug("poggReportPowerDataList.size={}",poggReportPowerDataList.size());
        return poggReportPowerDataList;
    }

    /**
     * 根据两点经纬度计算距离 km
     * @param l1
     * @param l2
     * @return
     */
    public Double calculateDistanceWithGPSLocation(Location l1, Location l2) {
        final double EARTH_RADIUS = 6371000.0; // 赤道半径 (单位 m)
        double radLat1 = l1.getLatitude() * Math.PI / 180.0;
        double radLat2 = l2.getLatitude() * Math.PI / 180.0;;
        double a = radLat1 - radLat2;
        double b = l1.getLongitude() * Math.PI / 180.0 - l2.getLongitude()  * Math.PI / 180.0;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        s = s / 1000;
        return s;
    }

    /**
     * 根据和目标 miner 的距离和数据完整度来找到最合适的对比用 miner
     * @param targetLocation
     * @param miners
     * @param powers
     * @return
     */
    public List<String> findSuitableMinerWithLocationAndPower(Location targetLocation, Map<String, Location> miners, Map<String, List<PoggReportPowerData>> powers) {
        // TODO(lq): 挪到配置文件里
        final int COMPARE_MIINER_NUM_MAX = 4;
        final Double SUIT_DISTANCE_MAX = 10.0;
        final Double SCORE_MAX = 10.0;
        final int SAMPLE_RECORD_NUM_MAX = 24 * 7 * 12; // 窗口期为 1 周，每小时最多 12 条。
        final Double DISTANCE_SCORE_WEIGHT= 0.6;
        final Double DATA_SCORE_WEIGHT= 0.4;

        Map<String, Double> minerScore = new HashMap<String, Double>();

        // 计算距离得分
        miners.forEach((key, value) -> {
            Double distance = calculateDistanceWithGPSLocation(targetLocation, value);
            distance = distance >= SUIT_DISTANCE_MAX ? SUIT_DISTANCE_MAX : Math.floor(distance);
            Double distanceScore = SCORE_MAX - distance;
            minerScore.put(key, distanceScore * DISTANCE_SCORE_WEIGHT);
        });

        // 计算数据得分
        powers.forEach((key, value) -> {
            int sampleRecordNum = value.size();
            Double dataScore = (sampleRecordNum / SAMPLE_RECORD_NUM_MAX) * SCORE_MAX;
            minerScore.put(key, minerScore.get(key) + dataScore * DATA_SCORE_WEIGHT);
        });
        
        // 按照得分排序并取前几位
        List<String> suitableMiner = minerScore.entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed()).limit(COMPARE_MIINER_NUM_MAX)
            .map(Map.Entry::getKey).collect(Collectors.toList());

        return suitableMiner;
    }

    /**
     * 找到合适的对比 miner
     * @param address
     * @param startEpoch
     * @param endEpoch
     * @return
     */
    public Map<String, List<PoggReportPowerData>> findSuitableCompareMiners(String address, long startEpoch, long endEpoch, Location location) {
        // TODO(lq): 挪到配置文件里
        final Double SUIT_DISTANCE_MAX = 10.0;

        Map<String, List<PoggReportPowerData>> suitableCompareMiners = new HashMap<String, List<PoggReportPowerData>>();

        // TODO(lq)：获取固定范围内的 miner 位置信息，而非全部。
        // 获取所有 miner 位置
        Result<HashMap>  minerLocationResult= remoteDeviceService.getMinerLocation();
        if (minerLocationResult.getCode() != ApiStatus.SUCCESS.getCode()) {
            log.error("getMinerLocation failed");
            return suitableCompareMiners;
        }
        if (minerLocationResult.getData() == null) {
            log.error("getMinerLocation found no miner");
            return suitableCompareMiners;
        }
        HashMap<String, Location> minerLocations = minerLocationResult.getData();
        log.debug("getMinerLocation found {} miners", minerLocations.size());

        // 根据目标 miner 位置找到一定范围内的所有备选 miner
        Location targetMinerLocation = minerLocations.get(address);
        Map<String, Location> locationSuitMiners = minerLocations.entrySet().stream().filter(item -> {
            if (item.getKey().equals(address)) {
                return false;
            }
            if (calculateDistanceWithGPSLocation(targetMinerLocation, item.getValue()) > SUIT_DISTANCE_MAX) {
                return false;
            }
            return true;
        }).collect(Collectors.toMap(Entry::getKey, Entry::getValue, (a,b)->b));
        log.debug("locationSuitMiners size: {}", locationSuitMiners.size());

        if (locationSuitMiners.size() == 0) {
            log.debug("not found location suitable miner");
            return suitableCompareMiners;
        }

        // 获取备选 miner 在 epoch 区间内的所有功率数据
        Map<String, List<PoggReportPowerData>> locationSuitMinerPowerData = new HashMap<String, List<PoggReportPowerData>>();
        locationSuitMiners.forEach((key, value) -> {
            List<PoggReportPowerData> poggReportPowerDatas = getMinerPowerDataByEpoch(key, startEpoch, endEpoch);
            locationSuitMinerPowerData.put(key, poggReportPowerDatas);
        });

        // 根据位置和数据筛选出合适的对比 miner
        List<String> suitableMiner = findSuitableMinerWithLocationAndPower(targetMinerLocation, locationSuitMiners, locationSuitMinerPowerData);
        log.debug("suitableMiner size: {}", suitableMiner.size());
        if (suitableMiner.size() == 0) {
            log.debug("not found suitable miner");
            return suitableCompareMiners;
        }

        // 组装
        suitableMiner.forEach(item -> {
            suitableCompareMiners.put(item, locationSuitMinerPowerData.get(item));
        }); 

        location = targetMinerLocation;

        return suitableCompareMiners;
    }

    /**
     * 将功率数据填充到窗口功率数组的合适位置，用零替代缺失的数据
     * @param data
     * @param startEpoch
     * @param endEpoch
     * @return
     */
    public Long[] fillPowerDataInWindowZeroArray(List<PoggReportPowerData> data, long startEpoch, long endEpoch) {
        // TODO(lq): 挪到配置文件里
        final int EPOCH_RECORD_NUM_MAX = 12; // 每个 epoch 为 1 小时，每个 epoch 最多 12 条。 
        final int WINDOW_RECORD_NUM_MAX = 24 * 7 * 12; // 窗口期为 1 周

        Long[] windowPowerData = new Long[WINDOW_RECORD_NUM_MAX];
        Arrays.fill(windowPowerData, 0.0);
        int lastIndex = 0;
        long lastEpoch = startEpoch;
        for (int i = 0; i < data.size(); i++) {
            PoggReportPowerData p = data.get(i);
            Long epoch = p.getEpoch();
            if (epoch.longValue() > endEpoch) {
                continue;
            }
            if (epoch.longValue() != lastEpoch) {
                lastIndex = 0;
            }
            Long index =  epoch - startEpoch;
            windowPowerData[index.intValue() * EPOCH_RECORD_NUM_MAX + lastIndex] = p.getPower();
            lastIndex++; lastEpoch++;
        }
        return windowPowerData;
    }

    /**
     * 标准化功率数据
     * @param data
     * @return
     */
    public Double[] standardizePowerData(Long[] data) {
        Double[] standardizedPowerData = new Double[data.length];
        double averagePower = Arrays.stream(data).reduce(Long.valueOf(0), (x,y) -> x+y) / data.length;
        for (int i = 0; i < data.length; i++) {
            standardizedPowerData[i] = data[i].longValue() / averagePower;
        }
        return standardizedPowerData;
    }

    /**
     * 计算向量余弦相似度
     * @param vector1
     * @param vector2
     * @return
     */
    public Double calculateVectorCosineSimilarity(Double[] vector1, Double[] vector2) {
        double sum = 0.0;
        Double v1Len = 0.0;
        Double v2Len = 0.0;
        int len = vector1.length;
        for (int i = 0; i < len; i++) {
            sum += vector1[i] * vector2[i];
            v1Len += vector1[i] * vector1[i];
            v2Len += vector2[i] * vector2[i];
        }
        return sum / (Math.sqrt(v1Len) * Math.sqrt(v2Len));
    }

    /**
     * 计算目标 miner 和对比 miner 的功率曲线相似度
     * @param target
     * @param compare
     * @param endEpoch
     * @param startEpoch
     * @return
     */
    public Double[] calculateSimilarity(List<PoggReportPowerData> target, Map<String, List<PoggReportPowerData>> compare, long startEpoch, long endEpoch) {
        // 填充 target miner 的数据并标准化
        Double[] targetMinerPowerData = standardizePowerData(fillPowerDataInWindowZeroArray(target, startEpoch, endEpoch));
        log.debug("targetMinerPowerData after standardize and fill: {}", JSONObject.toJSONString(targetMinerPowerData));

        // 填充 compare miner 的数据并标准化
        Map<String, Double[]> compareMinerPowerData = new HashMap<String, Double[]>();
        compare.forEach((key, value) -> {
            Long[] minerPowerData = fillPowerDataInWindowZeroArray(value, startEpoch, endEpoch);
            compareMinerPowerData.put(key, standardizePowerData(minerPowerData));
        });
        log.debug("compareMinerPowerData after standardize and fill: {}", JSONObject.toJSONString(targetMinerPowerData));

        // 相似度计算
        List<Double> similarityList = compareMinerPowerData.entrySet().stream().map(item -> {
            return calculateVectorCosineSimilarity(targetMinerPowerData, item.getValue());
        }).collect(Collectors.toList());
        log.debug("similarityList: {}", JSONObject.toJSONString(similarityList));

        return similarityList.toArray(new Double[similarityList.size()]);
    }

    /**
     * 根据相似度进行投票
     * @param similarityArray
     * @return
     */
    public int voteToSimilarity(Double[] similarityArray) {
        // TODO(lq): 挪到配置文件里
        final Double SIMILARITY_THRESHOLD = 0.5;

        int vote = 0;
        for (int i = 0; i < similarityArray.length; i++) {
            if (similarityArray[i] >= SIMILARITY_THRESHOLD) vote += 1;
        }
        return vote;
    }

    /**
     * 计算相似度得分
     * @param vote
     * @return
     */
    public Double calSimilarityScore(int voteNum) {
        return (double)1.0 / (1.0 + Math.exp(-voteNum));
    }

    /**
     * 构建 miner 的真实性数据
     * @param address
     * @param startEpoch
     * @param endEpoch
     * @return
     */
    public PoggAuthenticity constructPoggAuthenticity(String address, long startEpoch, long endEpoch) {
        Location location = new Location();

        // 获取 miner 在 epoch 区间内的所有功率数据
        List<PoggReportPowerData> poggReportPowerDatas = getMinerPowerDataByEpoch(address, startEpoch, endEpoch);

        // 找到合适的对比 miner，数量有上限但是不定。
        Map<String, List<PoggReportPowerData>> suitableCompareMiners = findSuitableCompareMiners(address, startEpoch, endEpoch, location);

        // 计算功率数据相似度
        Double[] similarityArray = calculateSimilarity(poggReportPowerDatas, suitableCompareMiners, startEpoch, endEpoch);

        // 根据相似度进行投票
        int voteNum = voteToSimilarity(similarityArray);
        
        // 根据投票计算 miner 相似度得分
        Double similarityScore = calSimilarityScore(voteNum);

        // 构建结构体
        PoggAuthenticity poggAuthenticity = new PoggAuthenticity();
        poggAuthenticity.setAddress(address);
        poggAuthenticity.setScore(similarityScore);
        poggAuthenticity.setLocation(location);
        poggAuthenticity.setPowerData(poggReportPowerDatas);
        poggAuthenticity.setCompareMinerPowerData(suitableCompareMiners);
        log.debug("poggAuthenticity: {}", JSONObject.toJSONString(poggAuthenticity));

        return poggAuthenticity;
    }

}
