package com.clemble.casino.server.rule.breach;

import com.clemble.casino.json.ObjectMapperUtils;
import com.clemble.casino.rule.breach.BreachPunishment;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

/**
 * Created by mavarazy on 9/5/14.
 */
@ReadingConverter
public class StringToBreachPunishment implements Converter<BreachPunishment, String> {

    @Override
    public String convert(BreachPunishment source) {
        try {
            String presentation = ObjectMapperUtils.OBJECT_MAPPER.writeValueAsString(source);
            return presentation;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }


}
