package com.clemble.casino.server.converters;

import com.clemble.casino.json.ObjectMapperUtils;
import com.clemble.casino.lifecycle.configuration.rule.breach.BreachPunishment;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

/**
 * Created by mavarazy on 9/5/14.
 */
@WritingConverter
public class BreachPunishmentToString implements Converter<BreachPunishment, String> {

    @Override
    public String convert(BreachPunishment source) {
        try {
            return ObjectMapperUtils.OBJECT_MAPPER.writeValueAsString(source);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
