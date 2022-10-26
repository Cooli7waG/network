package com.aitos.xenon.block.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.io.FileReader;
import java.io.FileWriter;

import org.junit.jupiter.api.*;

import com.aitos.xenon.block.domain.PoggReportPowerData;
import com.aitos.xenon.block.service.impl.PoggServiceImpl;
import com.alibaba.fastjson.JSONObject;
import com.opencsv.*;

import lombok.Data;

public class PoggServiceImplTest {
    private static final int EPOCH_RECORD_NUM_MAX = 12; // 每个 epoch 为 1 小时，每个 epoch 最多 12 条。
    private static final long START_EPOCH = 100;
    private static final long END_EPOCH = START_EPOCH + 24 * 7;
    private static final long POWER_MAX = 100;

    private static PoggServiceImpl poggServiceImpl;

    /**
     * 承载真实的功率数据
     */
    @Data
    static class PowerData {
        private Long epoch;
        private Long power;
        private Long timestamp;
    }

    public List<PowerData> readPowerDataFromCSV(String filePath) {
        List<PowerData> datas = new ArrayList<PowerData>();

        try {
            CSVReader reader = new CSVReaderBuilder(new FileReader(filePath)).build();
            String[] nextLine;
            nextLine = reader.readNext();
            while ((nextLine = reader.readNext()) != null) {
                PowerData powerData = new PowerData();
                powerData.setEpoch(Long.parseLong(nextLine[4]));
                powerData.setPower(Long.parseLong(nextLine[5]));
                powerData.setTimestamp(Long.parseLong(nextLine[8]));
                datas.add(powerData);
            }
        } catch (Exception e) {
            System.out.printf("parse csv failed: %s\n", e.toString());
            assert (false);
        }
        return datas;
    }

    public boolean writePowerDataToCSV(String filePath, List<PowerData> datas) {
        try {
            CSVWriter writer = (CSVWriter) new CSVWriterBuilder(new FileWriter(filePath)).withSeparator(',').build();
            String[] title = "epoch,power,timestamp".split(",");
            writer.writeNext(title);
            datas.forEach(item -> {
                String[] entry = new String[] { item.getEpoch().toString(), item.getPower().toString(),
                        item.getTimestamp().toString() };
                writer.writeNext(entry);
            });
            writer.close();
        } catch (Exception e) {
            System.out.printf("parse csv failed: %s\n", e.toString());
            return false;
        }
        return true;
    }

    public static List<PoggReportPowerData> deepCopyPowerData(List<PoggReportPowerData> source) {
        List<PoggReportPowerData> dest = new ArrayList<PoggReportPowerData>();
        source.forEach(item -> {
            PoggReportPowerData p = new PoggReportPowerData();
            p.setAddress(item.getAddress());
            p.setEpoch(item.getEpoch());
            p.setPower(item.getPower());
            p.setTimestamp(item.getTimestamp());
            dest.add(p);
        });
        return dest;
    }

    /**
     * 在 epoch 范围内的随机功率数据，每个 epoch 最多 EPOCH_RECORD_NUM_MAX 条
     * 
     * @param num
     * @param startEpoch
     * @param endEpoch
     * @return
     */
    public List<PoggReportPowerData> genRandomPowerData(int num, long startEpoch, long endEpoch) {
        if (num > (int) ((endEpoch - startEpoch + 1) * EPOCH_RECORD_NUM_MAX)) {
            num = (int) ((endEpoch - startEpoch + 1) * EPOCH_RECORD_NUM_MAX);
        }
        Random r = new Random();
        long epochScope = endEpoch - startEpoch + 1;
        int alreadyGenNum = 0;
        Map<Long, Integer> epochRecordMap = new HashMap<Long, Integer>();
        List<PoggReportPowerData> poggReportPowerDataList = new ArrayList<PoggReportPowerData>();

        while (alreadyGenNum < num) {
            Long e = r.nextLong(epochScope) + startEpoch;
            Integer n = epochRecordMap.get(e);
            if (n == null) {
                epochRecordMap.put(e, 0);
            } else if (epochRecordMap.get(e) >= EPOCH_RECORD_NUM_MAX) {
                continue;
            }
            PoggReportPowerData d = new PoggReportPowerData();
            d.setPower(r.nextLong(POWER_MAX));
            d.setEpoch(e);
            epochRecordMap.put(e, epochRecordMap.get(e) + 1);
            poggReportPowerDataList.add(d);
            alreadyGenNum++;
        }
        return poggReportPowerDataList;
    };

    /**
     * 将 List<PowerData> 转换成 List<PoggReportPowerData>
     * 
     * @param datas
     * @return
     */
    public List<PoggReportPowerData> recoverFromPowerData(List<PowerData> datas) {
        List<PoggReportPowerData> poggReportPowerDataList = new ArrayList<PoggReportPowerData>();

        for (PowerData data : datas) {
            PoggReportPowerData d = new PoggReportPowerData();
            d.setPower(data.getPower());
            d.setEpoch(data.getEpoch());
            d.setTimestamp(data.getTimestamp());
            poggReportPowerDataList.add(d);
        }
        return poggReportPowerDataList;
    };

    /**
     * 将 List<PoggReportPowerData> 转换成 List<PowerData>
     * 
     * @param datas
     * @return
     */
    public List<PowerData> convertToPowerData(List<PoggReportPowerData> datas) {
        List<PowerData> powerDataList = new ArrayList<PowerData>();

        for (PoggReportPowerData data : datas) {
            PowerData d = new PowerData();
            d.setPower(data.getPower());
            d.setEpoch(data.getEpoch());
            d.setTimestamp(data.getTimestamp());
            powerDataList.add(d);
        }
        return powerDataList;
    };

    /**
     * 随机按比例丢弃掉一些数据，模拟缺失部分数据的场景：
     * - 因为未工作而缺失
     * - 因为遮挡等原因缺失
     * 
     * @param datas
     * @param proportion 百分之几的概率丢弃
     */
    public void randomDropSomeData(List<PoggReportPowerData> datas, Integer proportion) {
        Random r = new Random();
        datas.forEach(item -> {
            if (item.getPower().longValue() != 0 && r.nextInt(100) < proportion) {
                item.setPower(Long.valueOf(0));
            }
        });
    }

    /**
     * 按比例连续丢弃掉一些数据，模拟缺失部分数据的场景：
     * - 因为未工作而缺失
     * - 因为遮挡等原因缺失
     * 
     * @param datas
     * @param proportion 百分之几的概率丢弃
     * @param skip       跳过开头的多少数据不丢弃
     */
    public void dropContinuousData(List<PoggReportPowerData> datas, Integer proportion, Integer skip) {
        Integer totalRecordNum = 0;
        for (int i = 0; i < datas.size(); i++) {
            PoggReportPowerData item = datas.get(i);
            if (item.getPower().longValue() != 0) {
                totalRecordNum++;
            }
        }
        Integer dropRecordNum = totalRecordNum * proportion / 100;
        Integer alreadyDropNum = 0;
        Integer skipNum = 0;
        for (int i = 0; i < datas.size(); i++) {
            PoggReportPowerData item = datas.get(i);
            if (item.getPower().longValue() != 0 && alreadyDropNum <= dropRecordNum) {
                if (skipNum >= skip) {
                    item.setPower(Long.valueOf(0));
                    alreadyDropNum++;
                } else {
                    skipNum++;
                }
            }
        }
    }

    @BeforeAll
    public static void setUp() {
        poggServiceImpl = new PoggServiceImpl();
    }

    @Test
    public void test_fillPowerDataInWindowZeroArray() {
        {
            List<PoggReportPowerData> d = genRandomPowerData(10, 0, 5);
            Long[] dF = poggServiceImpl.fillPowerDataInWindowZeroArray(d, 0, 5);
            assertEquals(6 * 12, dF.length);
            System.out.printf("dF, len: %d, %s\n", dF.length, JSONObject.toJSONString(dF, true));
        }
    }

    @Test
    public void test_standardizePowerData() {
        {
            Long[] d = new Long[] { Long.valueOf(1), Long.valueOf(1), Long.valueOf(1) };
            Double[] dS = poggServiceImpl.standardizePowerData(d);
            Double[] dE = new Double[] { Double.valueOf(1), Double.valueOf(1), Double.valueOf(1) };
            // System.out.printf("dS: %s, dE: %s\n", JSONObject.toJSONString(dS),
            // JSONObject.toJSONString(dE));
            assertEquals(true, Arrays.equals(dE, dS));
        }

        {
            Long[] d = new Long[] { Long.valueOf(4), Long.valueOf(1), Long.valueOf(1) };
            Double[] dS = poggServiceImpl.standardizePowerData(d);
            Double[] dE = new Double[] { Double.valueOf(2), Double.valueOf(0.5), Double.valueOf(0.5) };
            // System.out.printf("dS: %s, dE: %s\n", JSONObject.toJSONString(dS),
            // JSONObject.toJSONString(dE));
            assertEquals(true, Arrays.equals(dE, dS));
        }
    }

    @Test
    public void test_calculateVectorCosineSimilarity() {

        {
            Double[] vector1 = new Double[] { 0.0, 1.0 };
            Double[] vector2 = new Double[] { 1.0, 0.0 };
            Double similarity = poggServiceImpl.calculateVectorCosineSimilarity(vector1, vector2);
            assertEquals(0.0, similarity);
        }

        {
            Double[] vector1 = new Double[] { 0.0, 1.0 };
            Double[] vector2 = new Double[] { 0.0, 1.0 };
            Double similarity = poggServiceImpl.calculateVectorCosineSimilarity(vector1, vector2);
            assertEquals(1.0, similarity);
        }

        {
            Double[] vector1 = new Double[] { 0.0, 0.1 };
            Double[] vector2 = new Double[] { 1.0, 1.0 };
            Double similarity = poggServiceImpl.calculateVectorCosineSimilarity(vector1, vector2);
            assertEquals(0.70710678118, similarity, 0.0001);
        }
    }

    @Test
    public void test_voteToSimilarity() {
        {
            Double[] similarityArray = new Double[] { 0.5 };
            int voteNum = poggServiceImpl.voteToSimilarity(similarityArray);
            assertEquals(1, voteNum);
        }

        {
            Double[] similarityArray = new Double[] { 0.5, 0.6, 0.4 };
            int voteNum = poggServiceImpl.voteToSimilarity(similarityArray);
            assertEquals(2, voteNum);
        }

        {
            Double[] similarityArray = new Double[] { 0.4 };
            int voteNum = poggServiceImpl.voteToSimilarity(similarityArray);
            assertEquals(0, voteNum);
        }
    }

    @Test
    public void test_calSimilarityScore() {
        int voteNum = 0;
        Double similarityScore = poggServiceImpl.calSimilarityScore(voteNum);
        assertEquals(0.5, similarityScore);
    }

    @Test
    public void test_calculateSimilarity() {
        List<PoggReportPowerData> targePowerDataList = genRandomPowerData(1000, START_EPOCH, END_EPOCH);
        // System.out.printf("targePowerDataList: %s\n",
        // JSONObject.toJSONString(targePowerDataList));

        Map<String, List<PoggReportPowerData>> comparePowerDatas = new HashMap<String, List<PoggReportPowerData>>();
        String[] compareMinerAddress = { "a", "b", "c", "d" };
        for (String addr : compareMinerAddress) {
            comparePowerDatas.put(addr, genRandomPowerData(1000, START_EPOCH, END_EPOCH));
        }
        // System.out.printf("comparePowerDatas: %s\n",
        // JSONObject.toJSONString(comparePowerDatas));
        Double[] similarityArray = poggServiceImpl.calculateSimilarity(targePowerDataList, comparePowerDatas,
                START_EPOCH, END_EPOCH);
        System.out.printf("test_calculateSimilarity, similarityArray: %s\n",
                JSONObject.toJSONString(similarityArray, true));
    }

    /**
     * 根据真实功率数据，进行模拟测试
     */
    @Test
    public void test_authenticityWithRealPowerData() {
        // 读取 csv 文件
        String fileName = "power.csv";
        String filePath = "src/test/java/com/aitos/xenon/block/resources/" + fileName;
        List<PowerData> datas = readPowerDataFromCSV(filePath);

        // 确定 epoch 范围
        Long startEpoch = datas.get(0).getEpoch();
        Long endEpoch = datas.get(datas.size() - 1).getEpoch();

        System.out.printf("datas size: %d, startEpoch: %s, endEpoch: %s\n", datas.size(), startEpoch.toString(),
                endEpoch.toString());

        // 还原 compare miner 数据
        List<PoggReportPowerData> compareMinerPowerList = recoverFromPowerData(datas);
        // System.out.printf("compareMinerPowerList: %s\n",
        // JSONObject.toJSONString(compareMinerPowerList, true));

        // case 1: 4 个一样的 compare miner，target miner 和 compare mine 一样
        {
            // 生成 compare miner
            Map<String, List<PoggReportPowerData>> comparePowerDatas = new HashMap<String, List<PoggReportPowerData>>();
            String[] compareMinerAddress = { "a", "b", "c", "d" };
            for (String addr : compareMinerAddress) {
                comparePowerDatas.put(addr, compareMinerPowerList);
            }
            // 生成 target miner
            List<PoggReportPowerData> targetMinerPowerList = compareMinerPowerList;

            // 计算相似度得分
            Double[] similarityArray = poggServiceImpl.calculateSimilarity(targetMinerPowerList, comparePowerDatas,
                    startEpoch, endEpoch);
            System.out.printf("test_authenticityWithRealPowerData, similarityArray: %s\n",
                    JSONObject.toJSONString(similarityArray, true));

            // 将测试数据写入 CSV
            String p = "/tmp/testCase1Power.csv";
            writePowerDataToCSV(p, convertToPowerData(targetMinerPowerList));
        }

        // case 2: 4 个一样的 compare miner，target miner 和 compare mine 曲线一样，但是幅度减半。
        {
            try {
                // 生成 compare miner
                Map<String, List<PoggReportPowerData>> comparePowerDatas = new HashMap<String, List<PoggReportPowerData>>();
                String[] compareMinerAddress = { "a", "b", "c", "d" };
                for (String addr : compareMinerAddress) {
                    comparePowerDatas.put(addr, compareMinerPowerList);
                }
                // 生成 target miner
                List<PoggReportPowerData> targetMinerPowerList = deepCopyPowerData(compareMinerPowerList);
                targetMinerPowerList.forEach(item -> {
                    item.setPower(item.getPower() / 2);
                });

                // 计算相似度得分
                Double[] similarityArray = poggServiceImpl.calculateSimilarity(targetMinerPowerList, comparePowerDatas,
                        startEpoch, endEpoch);
                System.out.printf("test_authenticityWithRealPowerData, similarityArray: %s\n",
                        JSONObject.toJSONString(similarityArray, true));

                // 将测试数据写入 CSV
                String p = "/tmp/testCase2Power.csv";
                writePowerDataToCSV(p, convertToPowerData(targetMinerPowerList));
            } catch (Exception e) {
                System.out.printf("somethine wrong: %s\n", e.toString());
                assert (false);
            }
        }

        // case 3: 4 个一样的 compare miner，target miner 和 compare mine 大体一致，但是有些数据缺失。
        {
            try {
                // 生成 compare miner
                Map<String, List<PoggReportPowerData>> comparePowerDatas = new HashMap<String, List<PoggReportPowerData>>();
                String[] compareMinerAddress = { "a", "b", "c", "d" };
                for (String addr : compareMinerAddress) {
                    comparePowerDatas.put(addr, compareMinerPowerList);
                }
                // 80% 几率
                {
                    List<PoggReportPowerData> targetMinerPowerList = deepCopyPowerData(compareMinerPowerList);
                    randomDropSomeData(targetMinerPowerList, 80);
                    Double[] similarityArray = poggServiceImpl.calculateSimilarity(targetMinerPowerList,
                            comparePowerDatas,
                            startEpoch, endEpoch);
                    System.out.printf("test_authenticityWithRealPowerData, 80, similarityArray: %s\n",
                            JSONObject.toJSONString(similarityArray, true));
                    String p = "/tmp/testCase3-1Power.csv";
                    writePowerDataToCSV(p, convertToPowerData(targetMinerPowerList));
                }
                // 50% 几率
                {
                    List<PoggReportPowerData> targetMinerPowerList = deepCopyPowerData(compareMinerPowerList);
                    randomDropSomeData(targetMinerPowerList, 50);
                    Double[] similarityArray = poggServiceImpl.calculateSimilarity(targetMinerPowerList,
                            comparePowerDatas,
                            startEpoch, endEpoch);
                    System.out.printf("test_authenticityWithRealPowerData, 50, similarityArray: %s\n",
                            JSONObject.toJSONString(similarityArray, true));
                    String p = "/tmp/testCase3-2Power.csv";
                    writePowerDataToCSV(p, convertToPowerData(targetMinerPowerList));
                }
                // 10% 几率随机丢弃
                {
                    List<PoggReportPowerData> targetMinerPowerList = deepCopyPowerData(compareMinerPowerList);
                    randomDropSomeData(targetMinerPowerList, 10);
                    Double[] similarityArray = poggServiceImpl.calculateSimilarity(targetMinerPowerList,
                            comparePowerDatas,
                            startEpoch, endEpoch);
                    System.out.printf("test_authenticityWithRealPowerData, 10, similarityArray: %s\n",
                            JSONObject.toJSONString(similarityArray, true));
                    String p = "/tmp/testCase3-3Power.csv";
                    writePowerDataToCSV(p, convertToPowerData(targetMinerPowerList));
                }
                // 10% 几率连续丢弃，从头开始丢弃
                {
                    List<PoggReportPowerData> targetMinerPowerList = deepCopyPowerData(compareMinerPowerList);
                    dropContinuousData(targetMinerPowerList, 10, 0);
                    Double[] similarityArray = poggServiceImpl.calculateSimilarity(targetMinerPowerList,
                            comparePowerDatas,
                            startEpoch, endEpoch);
                    System.out.printf("test_authenticityWithRealPowerData, 10 cont, similarityArray: %s\n",
                            JSONObject.toJSONString(similarityArray, true));
                    String p = "/tmp/testCase3-4Power.csv";
                    writePowerDataToCSV(p, convertToPowerData(targetMinerPowerList));
                }
                // 10% 几率连续丢弃，跳过 20 之后开始丢弃
                // TODO(lq): 这个的输出图形不对
                {
                    List<PoggReportPowerData> targetMinerPowerList = deepCopyPowerData(compareMinerPowerList);
                    dropContinuousData(targetMinerPowerList, 10, 200);
                    Double[] similarityArray = poggServiceImpl.calculateSimilarity(targetMinerPowerList,
                            comparePowerDatas,
                            startEpoch, endEpoch);
                    System.out.printf("test_authenticityWithRealPowerData, 10 cont skip, similarityArray: %s\n",
                            JSONObject.toJSONString(similarityArray, true));
                    String p = "/tmp/testCase3-5Power.csv";
                    writePowerDataToCSV(p, convertToPowerData(targetMinerPowerList));
                }
            } catch (Exception e) {
                System.out.printf("somethine wrong: %s\n", e.toString());
                assert (false);
            }
        }
    }
}
