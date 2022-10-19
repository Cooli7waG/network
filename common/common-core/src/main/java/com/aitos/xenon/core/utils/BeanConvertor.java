package com.aitos.xenon.core.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.validation.FieldError;

import java.lang.reflect.Field;
import java.util.*;

public class BeanConvertor {

    public static <T> T toBean(Object o,Class<T> cls){
        if(o==null){
            return null;
        }
        T newO= null;
        try {
            newO = cls.newInstance();
            BeanUtils.copyProperties(o,newO);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return newO;
    }
    public static <T> T toBean(Map<String, Object> map, Class<T> cls){
        if(map==null){
            return null;
        }
        try {
            T newO = cls.newInstance();
            Field[] parenttFields = cls.getSuperclass().getDeclaredFields();
            Field[] fields = cls.getDeclaredFields();
            for(Field field : fields) {
                Class fc = field.getType();
                field.setAccessible(true);

                String fieldName=field.getName();
                Object value = map.get(fieldName);
                if(value!=null){
                    if(fc.isPrimitive()){
                        field.set(newO, value);
                    }else{
                        if(fc.isAssignableFrom(Date.class)){
                            Date  date=new Date();
                            date.setTime(Long.valueOf(value.toString()));
                            field.set(newO, date);
                        }else{
                            field.set(newO, value);
                        }
                    }
                }
            }

            for(Field field : parenttFields) {
                Class fc = field.getType();
                field.setAccessible(true);

                String fieldName=field.getName();
                Object value = map.get(fieldName);
                if(value!=null){
                    if(fc.isPrimitive()){
                        field.set(newO, value);
                    }else{
                        if(fc.isAssignableFrom(Date.class)){
                            Date  date=new Date();
                            date.setTime(Long.valueOf(value.toString()));
                            field.set(newO, date);
                        }else{
                            field.set(newO, value);
                        }
                    }
                }
            }
            return newO;
        } catch (Exception e) {
            System.out.println("transMap2Bean Error " + e);
        }
        return null;
    }
    public static <T> List<T> toList(List<?> list, Class<T> cls){
        if(list==null){
            return null;
        }
        List<T> newList=new ArrayList<>();
        list.stream().forEach(item->{
            T t=toBean(item,cls);
            newList.add(t);
        });
        return newList;
    }


    /**
     *校验错误信息转换
     * @param allErrors
     * @return
     */
    public static List<HashMap<String,Object>> validErrorMessage(List<FieldError> allErrors){
        List<HashMap<String,Object>>  list=new ArrayList<>();
        allErrors.stream().forEach(item->{
            HashMap<String,Object>  map=new HashMap<String,Object>();
            map.put("field",item.getField());
            map.put("msg",item.getDefaultMessage());
            list.add(map);
        });
        return list;
    }
}