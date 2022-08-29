package com.aitos.xenon.push.mapper;

import com.aitos.xenon.push.domain.PushTemplate;
import org.apache.ibatis.annotations.Param;

public interface PushTemplateMapper {

    PushTemplate findById(@Param("id") Long id);
}
