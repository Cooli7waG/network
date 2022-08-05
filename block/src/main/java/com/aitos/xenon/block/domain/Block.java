package com.aitos.xenon.block.domain;

import com.aitos.xenon.core.model.BaseModel;
import lombok.Data;

@Data
public class Block extends BaseModel {
       private  Long height;

       /**
        * 成块间隔，上一个块成块时间到本次成块时间间隔
        */
       private Long blockIntervalTime;
}
