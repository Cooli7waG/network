package com.aitos.xenon.core.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;

@Setter
@Getter
public class QueryParams implements Serializable {

    private HashMap<String,Object> params=new HashMap<>();

    private String  orderByName;
    private String orderByDesc;

    private int offset=1;

    private int limit=10;

    private int pageNum;
    private int pageSize;

    public int getPageNum() {
        return (getOffset()-1)*limit;
    }

    public int getPageSize() {
        return getLimit();
    }

    public void addParams(String key, Object value){
        params.put(key,value);
    }
}
