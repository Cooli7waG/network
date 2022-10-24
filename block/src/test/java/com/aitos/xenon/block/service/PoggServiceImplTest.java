package com.aitos.xenon.block.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.jupiter.api.*;

import com.aitos.xenon.block.domain.PoggReportPowerData;
import com.aitos.xenon.block.service.impl.PoggServiceImpl;
import com.alibaba.fastjson.JSONObject;

public class PoggServiceImplTest {
    private static final int EPOCH_RECORD_NUM_MAX = 12; // 每个 epoch 为 1 小时，每个 epoch 最多 12 条。
    private static final long START_EPOCH = 100;
    private static final long END_EPOCH = START_EPOCH + 24 * 7;
    private static final long POWER_MAX = 100;

    private static PoggServiceImpl poggServiceImpl;

    @BeforeAll
    public static void setUp() {
        poggServiceImpl = new PoggServiceImpl();
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
        System.out.printf("similarityArray: %s\n", JSONObject.toJSONString(similarityArray, true));
    }
}
