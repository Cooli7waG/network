package com.aitos.xenon.block.service;

import com.aitos.xenon.account.api.domain.dto.PoggRewardDetailDto;
import com.aitos.xenon.block.api.domain.dto.PoggGreenDataDto;
import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class PoggServiceTest {

    @Autowired
    private PoggService  poggService;

    @Test
    public void test_commit(){
        poggService.commit();
    }

    @Test
    public void test_green_data_convertor(){

        List<String> greenDataList=new ArrayList<>();
        greenDataList.add("010062df65b90000000000640000000000002710");
        List<PoggGreenDataDto> list = PoggGreenDataDto.decode(greenDataList);
        System.out.println(list);
    }

    @Test
    public void test_rewardCalculation(){
        poggService.rewardCalculation();
    }

    @Test
    public void test_giveOutRewards(){
        poggService.giveOutRewards();
    }

    @Test
    public void test_2(){
        System.out.println(LocalDateTime.now().getMinute());
    }

    @Test
    public void test_processAwardEligibility(){
        String commitPrivatekey="3hupFSQNWwiJuQNc68HiWzPgyNpQA2yy9iiwhytMS7rZyfCJDNrSLBqS8QguVBgam5TLWqgRFwSME86GUHpJrfGxqzgQLGB99cmU8FxzvWEg3WTGUTuCrp9XuRyJ5Sdej62WzJSVcr6Mmj9utPApB4VsqWMY4Z74v8xQx78t8wQmTR2FeBeurwAPzeJuMWB72xzA9";
        String minerAddress="PZ8Tyr4Nx8MHsRAGMpZmZ6TWY63dXWSCyYX7kae74h2wDin6wmJwpbMqGUrKxMf2FQA3nw616bhpmXKrEEQ5A3KkcY793AsKpF7EA5Rf1Yq1scnPAXunZEQd";
        Integer total=200;
        //poggService.processAwardEligibility(commitPrivatekey,minerAddress,total);
    }
    @Test
    public void test_json(){
        List<PoggRewardDetailDto> poggRewardDetailDtos = JSON.parseArray("[]", PoggRewardDetailDto.class);
        System.out.println(poggRewardDetailDtos);
    }
}
