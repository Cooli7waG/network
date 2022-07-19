package com.aitos.xenon.block.controller;

import com.aitos.xenon.block.api.domain.dto.PoggResponseDto;
import com.aitos.xenon.block.api.domain.vo.PoggChallengeVo;
import com.aitos.xenon.block.domain.PoggChallenge;
import com.aitos.xenon.block.domain.PoggChallengeRecord;
import com.aitos.xenon.block.service.PoggService;
import com.aitos.xenon.common.crypto.ed25519.Base58;
import com.aitos.xenon.common.crypto.ed25519.Ed25519;
import com.aitos.xenon.core.constant.ApiStatus;
import com.aitos.xenon.core.model.Result;
import com.aitos.xenon.core.utils.BeanConvertor;
import com.aitos.xenon.device.api.RemoteDeviceService;
import com.aitos.xenon.device.api.domain.vo.DeviceVo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/pogg")
public class PoggController {

    @Autowired
    private PoggService poggService;

    @Autowired
    private RemoteDeviceService remoteDeviceService;

    @Value("${pogg.challengeHit}")
    private Integer challengeHit;

    @GetMapping("/activeChallenges")
    public Result<List<PoggChallengeVo>> activeChallenges(){
        List<PoggChallenge> poggChallenge=poggService.activeChallenges();
        List<PoggChallengeVo>  poggChallengeVo= BeanConvertor.toList(poggChallenge,PoggChallengeVo.class);
        return Result.ok(poggChallengeVo);
    }

    @GetMapping("/queryChallenges")
    public Result<PoggChallengeVo> queryChallenges(String txHash){
        PoggChallenge poggChallenge=poggService.queryChallenges(txHash);
        PoggChallengeVo  poggChallengeVo= BeanConvertor.toBean(poggChallenge,PoggChallengeVo.class);
        return Result.ok(poggChallengeVo);
    }



    @PostMapping("/response")
    public Result response(@RequestBody String body){

        JSONObject paramsObject = JSONObject.parseObject(body, Feature.OrderedField);

        String address= paramsObject.getString("address");
        String randomSignature=paramsObject.getString("randomSignature");
        byte[] random= Hex.decode(paramsObject.getString("random"));

        byte[] signature=Base58.decode(paramsObject.getString("signature"));

        paramsObject.remove("signature");
        String data=paramsObject.toJSONString();

        if(!Ed25519.verifyBase58(address,random,randomSignature)){
            return Result.failed(ApiStatus.BUSINESS_POGG_RANDOM_SIGN_ERROR);
        }


        if(!Ed25519.verify(address,data.getBytes(),signature)){
            return Result.failed(ApiStatus.BUSINESS_POGG_RESPONSE_SIGN_ERROR);
        }

        PoggResponseDto poggResponseDto= JSON.parseObject(body,PoggResponseDto.class);

        //判断挑战是否过期
        List<PoggChallenge> poggChallenge=poggService.activeChallenges();
        long count=poggChallenge.stream().filter(item->item.getRandom().equals(poggResponseDto.getRandom())).count();
        if(count==0){
            return Result.failed(ApiStatus.BUSINESS_POGG_CHALLENGE_EXPIRED);
        }

        //判断提交PoGG响应的Miner是已经完成onboard并且未terminate的Miner
        Result<DeviceVo>  deviceVoResult= remoteDeviceService.queryByMiner(address);
        if(deviceVoResult.getCode()==ApiStatus.SUCCESS.getCode()&& deviceVoResult.getData()!=null){
            DeviceVo deviceVo= deviceVoResult.getData();
            if(deviceVo.getAccountId()==null){
                return Result.failed(ApiStatus.BUSINESS_DEVICE_NO_ONBOARD);
            }else if(deviceVo.getTerminate()==1){
                return Result.failed(ApiStatus.BUSINESS_DEVICE_TERMINATE);
            }
        }else{
            return Result.failed(ApiStatus.BUSINESS_DEVICE_NOT_EXISTED);
        }
        //判断是否被命中
        if(!challengeHit(poggResponseDto.getChallengeHash(),random,randomSignature)){
            return Result.failed(ApiStatus.BUSINESS_POGG_CHALLENGE_NO_HIT);
        }
        PoggChallengeRecord poggChallengeRecordTemp= poggService.findChallengeRecordByRandom(poggResponseDto.getAddressToHex(),poggResponseDto.getRandom());
        if(poggChallengeRecordTemp!=null){
            return Result.failed(ApiStatus.REPEAT_RECORD);
        }

        PoggChallengeRecord poggChallengeRecord=new PoggChallengeRecord();
        poggChallengeRecord.setRandom(poggResponseDto.getRandom());
        poggChallengeRecord.setAddress(poggResponseDto.getAddressToHex());
        poggChallengeRecord.setDeviceId(deviceVoResult.getData().getId());
        poggChallengeRecord.setData(body);
        String txHash=poggService.saveChallengeRecord(poggChallengeRecord);
        return Result.ok(txHash);
    }

    private boolean challengeHit(String challengeHash,byte[] random,String randomSignature){

        byte[] challengeHashBytes=Hex.decode(challengeHash);
        byte[] randomSignatureBytes=Base58.decode(randomSignature);

        byte[]  data=new byte[challengeHashBytes.length+random.length+randomSignatureBytes.length];
        System.arraycopy(challengeHashBytes, 0, data, 0, challengeHashBytes.length);
        System.arraycopy(random, 0, data, challengeHashBytes.length, random.length);
        System.arraycopy(randomSignatureBytes, 0, data, challengeHashBytes.length+random.length, randomSignatureBytes.length);

        BigInteger bigInteger = new BigInteger(DigestUtils.sha256(data));

        BigInteger result=bigInteger.remainder(new BigInteger(challengeHit.toString()));
        return result.equals(new BigInteger("0"))||result.equals(new BigInteger("-16"));
    }
}
