package com.clemble.casino.server.converters;

import com.clemble.casino.json.ObjectMapperUtils;
import com.clemble.casino.rule.bet.BetRule;
import org.springframework.core.convert.converter.Converter;

import java.io.IOException;

/**
 * Created by mavarazy on 9/6/14.
 */
public class StringToBetRule implements Converter<String, BetRule> {

    @Override
    public BetRule convert(String source) {
        try {
            return ObjectMapperUtils.OBJECT_MAPPER.readValue(source, BetRule.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
