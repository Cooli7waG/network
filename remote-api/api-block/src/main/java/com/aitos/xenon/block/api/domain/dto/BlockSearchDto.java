package com.aitos.xenon.block.api.domain.dto;

import com.aitos.xenon.core.model.QueryParams;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BlockSearchDto extends QueryParams {
       private  Long height;
}