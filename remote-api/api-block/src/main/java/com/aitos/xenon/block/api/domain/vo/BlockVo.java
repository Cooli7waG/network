package com.aitos.xenon.block.api.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BlockVo {
       private Long id;
       private String hash;
       private  Long height;
       private  Long amountTransaction;

       /**
        * 成块间隔，上一个块成块时间到本次成块时间间隔
        */
       private Long blockIntervalTime;

       private String parentHash;

       private String merkleRoot;

       private LocalDateTime createTime;

       private LocalDateTime updateTime;
}
