package com.aitos.xenon.core.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public final class BigDecimalSerialize extends JsonSerializer<BigDecimal> {
    private BigDecimalSerialize(){}

    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if(value!=null){
            BigDecimal number = value.setScale(2, RoundingMode.HALF_UP);
            gen.writeNumber(number);
        }else {
            gen.writeNumber(value);
        }
    }
}
