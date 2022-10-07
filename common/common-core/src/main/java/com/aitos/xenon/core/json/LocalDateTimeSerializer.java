package com.aitos.xenon.core.json;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class LocalDateTimeSerializer implements ObjectSerializer {
    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) {
        if (object != null) {
            if (object instanceof LocalDateTime) {
                LocalDateTime localDateTime = (LocalDateTime) object;
                long timestamp = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                serializer.write(timestamp);
                return;
            }
            throw new RuntimeException("date type exception");
        } else {
            serializer.out.writeNull();
        }
    }
}
