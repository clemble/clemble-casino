package com.clemble.casino.server.converters;

import com.clemble.casino.game.lifecycle.configuration.GameConfiguration;
import com.clemble.casino.json.ObjectMapperUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableSet;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import java.io.IOException;
import java.util.Set;

/**
 * Created by mavarazy on 8/30/14.
 */
public class GameConfigurationConverter  implements GenericConverter {

    final private Set<ConvertiblePair> TYPES = ImmutableSet.<ConvertiblePair> of(new ConvertiblePair(GameConfiguration.class, String.class), new ConvertiblePair(String.class, GameConfiguration.class));

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return TYPES;
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        // Step 1. Converting if source type is string
        if (source instanceof String) {
            // Step 1. Splitting source to parts
            String[] parts = ((String) source).split(":");
            // Step 2. Generating appropriate GameSessionKey
            try {
                return ObjectMapperUtils.OBJECT_MAPPER.readValue((String) source, GameConfiguration.class);
            } catch (IOException e) {
                return null;
            }
        } else {
            try {
                return ObjectMapperUtils.OBJECT_MAPPER.writeValueAsString(source);
            } catch (JsonProcessingException e) {
                return "";
            }
        }
    }

}
