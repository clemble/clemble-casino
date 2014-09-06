package com.clemble.casino.server.converters;

import com.clemble.casino.json.ObjectMapperUtils;
import com.clemble.casino.rule.bet.BetRule;
import com.clemble.casino.rule.breach.BreachPunishment;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.core.convert.converter.Converter;

/**
 * Created by mavarazy on 9/6/14.
 */
public class BetRuleToString implements Converter<BetRule, String>{

    @Override
    public String convert(BetRule source) {
        try {
            return ObjectMapperUtils.OBJECT_MAPPER.writeValueAsString(source);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
