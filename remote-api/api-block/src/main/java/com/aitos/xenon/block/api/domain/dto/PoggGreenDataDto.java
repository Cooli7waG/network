package com.aitos.xenon.block.api.domain.dto;

import com.aitos.xenon.core.exceptions.ParamValidateInvalidException;
import lombok.Data;
import org.bouncycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author hnngm
 */
@Data
public class PoggGreenDataDto {

    private int version;

    private long timestamp;

    private long power;

    private long total;

    private LocalDateTime createTime;

    public static List<PoggGreenDataDto> decode(List<String> dataList){
        List<PoggGreenDataDto>  list=new ArrayList<>();
        try{
            dataList.forEach(item->{
                byte[] data= Hex.decode(item);
                PoggGreenDataDto poggGreenDataDto=new PoggGreenDataDto();
                poggGreenDataDto.setVersion(data[0]);

                poggGreenDataDto.setTimestamp(new BigInteger(Arrays.copyOfRange(data,1,6)).longValue()*1000);
                poggGreenDataDto.setPower(new BigInteger(Arrays.copyOfRange(data,6,12)).longValue());
                poggGreenDataDto.setTotal(new BigInteger(Arrays.copyOfRange(data,12,20)).longValue());
                poggGreenDataDto.setCreateTime(LocalDateTime.now());
                list.add(poggGreenDataDto);
            });
        }catch (Exception e){
            throw new ParamValidateInvalidException(e.getMessage());
        }
        return list;
    }
}
