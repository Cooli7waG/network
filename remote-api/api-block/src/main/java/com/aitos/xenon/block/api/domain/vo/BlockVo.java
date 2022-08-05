package com.aitos.xenon.block.api.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BlockVo {
       private  Long height;
       /**
        * Transaction 数量
        */
       private int amountTransaction;
       /**
        * 成块间隔
        */
       private int blockIntervalTime;

       private LocalDateTime createTime;
}