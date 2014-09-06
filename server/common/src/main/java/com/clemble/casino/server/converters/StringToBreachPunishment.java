package com.clemble.casino.server.converters;

import com.clemble.casino.json.ObjectMapperUtils;
import com.clemble.casino.rule.breach.BreachPunishment;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.io.IOException;

/**
 * Created by mavarazy on 9/5/14.
 */
@ReadingConverter
public class StringToBreachPunishment implements Converter<String, BreachPunishment> {

    @Override
    public BreachPunishment convert(String source) {
        try {
            return ObjectMapperUtils.OBJECT_MAPPER.readValue(source, BreachPunishment.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
