package com.clemble.casino.server.rule.breach;

import com.clemble.casino.json.ObjectMapperUtils;
import com.clemble.casino.rule.breach.BreachPunishment;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.io.IOException;

/**
 * Created by mavarazy on 9/5/14.
 */
@WritingConverter
public class BreachPunishmentToString implements Converter<String, BreachPunishment> {

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
