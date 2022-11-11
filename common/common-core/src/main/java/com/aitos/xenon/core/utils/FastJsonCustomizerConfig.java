package com.aitos.xenon.core.utils;

import com.aitos.xenon.core.json.JSONFieldToHexStringFilter;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.*;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

//@Configuration
public class FastJsonCustomizerConfig {
    @Bean
    @Primary
    public HttpMessageConverters fastJsonHttpMessageConverter(){
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        //config.setDateFormat("yyyy-MM-dd");
        config.setCharset(Charset.forName("UTF-8"));
        config.setSerializerFeatures(
                //输出类名
                //SerializerFeature.WriteClassName,
                //输出map中value为null的数据
                SerializerFeature.WriteMapNullValue,
                //json格式化
                SerializerFeature.PrettyFormat,
                //输出空list为[]，而不是null
                SerializerFeature.WriteNullListAsEmpty,
                //输出空string为""，而不是null
                SerializerFeature.WriteNullStringAsEmpty
        );
        ValueFilter valueFilter = (object, name, value) -> {
            if (null == value){
                value = "";
            }
            return value;
        };
        config.setSerializeFilters(valueFilter,new JSONFieldToHexStringFilter());
        converter.setFastJsonConfig(config);

        SerializeConfig serializeConfig = SerializeConfig.globalInstance;
        serializeConfig.put(LocalDateTime.class, new LocalDateTimeSerializer());
        serializeConfig.put(Date.class, new DateSerializer());
        serializeConfig.put(BigDecimal.class, new BigDecimalSerializer());
        serializeConfig.put(BigInteger.class, new BigIntegerSerializer());
        config.setSerializeConfig(serializeConfig);


        //ParserConfig.getGlobalInstance().putDeserializer(AlarmString.class,AlarmStringDeserializer.instance);
        ParserConfig parserConfig= ParserConfig.getGlobalInstance();
        parserConfig.putDeserializer(LocalDateTime.class,new LocalDateTimeDeserializer());
        parserConfig.putDeserializer(Date.class,new DateDeserializer());
        parserConfig.putDeserializer(BigDecimal.class,new BigDecimalDeserializer());
        parserConfig.putDeserializer(BigInteger.class,new BigIntegerDeserializer());

        config.setParserConfig(parserConfig);

        HttpMessageConverter<?> converter2 = converter;
        return new HttpMessageConverters(converter2);
    }

    /**
     * description:序列化
     * LocalDateTime序列化为毫秒级时间戳
     */
    public static class LocalDateTimeSerializer implements ObjectSerializer {
        @Override
        public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
            SerializeWriter out = serializer.out;
            if (object == null) {
                out.writeNull();
            } else {
                LocalDateTime result = (LocalDateTime) object;
                long timestamp = result.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                out.writeLong(timestamp);
            }
        }
    }

    /**
     * description:反序列化
     * 毫秒级时间戳序列化为LocalDateTime
     */
    public static class LocalDateTimeDeserializer implements ObjectDeserializer {
        @Override
        public  LocalDateTime deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
            Long timestamp = parser.parseObject(long.class);
            String timestr= parser.parseObject(String.class);
            if (timestamp > 0) {
                return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
            }else if(StringUtils.hasText(timestr)){
                LocalDateTime dateTime=LocalDateTime.parse(timestr, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
                return dateTime;
            } else {
                return null;
            }
        }

        @Override
        public int getFastMatchToken() {
            return JSONToken.LITERAL_INT;
        }
    }

    /**
     * 序列化
     */
    public static class DateSerializer implements ObjectSerializer {
        @Override
        public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
            SerializeWriter out = serializer.out;
            if (object == null) {
                out.writeNull();
            } else {
                Date result = (Date) object;
                out.writeLong(result.getTime());
            }
        }
    }

    /**
     * 反序列化
     */
    public static class DateDeserializer implements ObjectDeserializer {
        @Override
        public  Date deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
            Long timestamp = parser.parseObject(long.class);
            String timestr= parser.parseObject(String.class);
            if (timestamp > 0) {
                Date date=new Date();
                date.setTime(timestamp);
                return date;
            }else if(StringUtils.hasText(timestr)){
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                try {
                    return formatter.parse(timestr);
                } catch (ParseException e) {
                    return null;
                }
            } else {
                return null;
            }
        }

        @Override
        public int getFastMatchToken() {
            return JSONToken.LITERAL_INT;
        }
    }

    public static class BigDecimalSerializer implements ObjectSerializer{
        @Override
        public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
            SerializeWriter out = serializer.out;
            if (object == null) {
                out.writeNull();
            } else {
                BigDecimal result = (BigDecimal) object;
                out.writeString(result.stripTrailingZeros().toPlainString());
            }
        }
    }

    public static class BigDecimalDeserializer implements ObjectDeserializer{
        @Override
        public  BigDecimal deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
            String text= parser.parseObject(String.class);
            if(StringUtils.hasText(text)){
                return new BigDecimal(text);
            }
            return null;
        }

        @Override
        public int getFastMatchToken() {
            return JSONToken.LITERAL_INT;
        }
    }


    public static class BigIntegerSerializer implements ObjectSerializer{
        @Override
        public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
            SerializeWriter out = serializer.out;
            if (object == null) {
                out.writeNull();
            } else {
                BigInteger result = (BigInteger) object;
                out.writeString(result.toString());
            }
        }
    }

    public static class BigIntegerDeserializer implements ObjectDeserializer{
        @Override
        public  BigInteger deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
            String text= parser.parseObject(String.class);
            if(StringUtils.hasText(text)){
                return new BigInteger(text);
            }
            return null;
        }

        @Override
        public int getFastMatchToken() {
            return JSONToken.LITERAL_INT;
        }
    }
}
