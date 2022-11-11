package com.aitos.xenon.core.json;

import com.alibaba.fastjson.serializer.ValueFilter;
import org.apache.commons.codec.binary.Hex;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

public class JSONFieldToHexStringFilter implements ValueFilter {
    private Field field = null;
    @Override
    public Object process(Object obj, String name, Object value) {
        JSONFieldToHexString annotation;
        try {
            field = obj.getClass().getDeclaredField(name);
            Type type =field.getGenericType();
            // 获取注解
            annotation = field.getAnnotation(JSONFieldToHexString.class);
            if (annotation != null) {
                if(type.toString().equals("class java.lang.String")){
                    return Hex.encodeHexString(value.toString().getBytes(StandardCharsets.UTF_8));
                }else if(type.toString().equals("class java.lang.Integer")){
                    return Integer.toHexString((Integer) value);
                }else if(type.toString().equals("class java.lang.Long")){
                    return Long.toHexString((Long) value);
                }else if(type.toString().equals("class java.lang.Float")){
                    return Float.toHexString((Float) value);
                }else if(type.toString().equals("class java.lang.Double")){
                    return Double.toHexString((Double) value);
                }else if(type.toString().equals("class java.lang.Short")){
                    return Integer.toHexString((Short) value);
                }else if(type.toString().equals("class java.lang.Boolean")){
                    return Integer.toHexString((Short) value);
                }
            }
        } catch (NoSuchFieldException e) {
            return value;
        } catch (Exception e) {
            return value;
        }
        return value;
    }
}