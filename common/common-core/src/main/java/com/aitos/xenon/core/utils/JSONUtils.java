package com.aitos.xenon.core.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

import java.util.List;

public class JSONUtils {
    public static String getJSON(Object data, List<String> includes, List<String> excludes){
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        if(includes!=null&&includes.size()>0){
            filter.getIncludes().addAll(includes);
        }
        if(excludes!=null&&excludes.size()>0){
            filter.getExcludes().addAll(excludes);
        }
        String jsonData= JSON.toJSONString(data,filter);
        return jsonData;
    }
}
