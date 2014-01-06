package com.clemble.casino.server.converters;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.Set;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import com.clemble.casino.game.specification.GameSpecification;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableSet;

public class GameSpecificationConverter implements GenericConverter {

    final private Set<ConvertiblePair> TYPES = ImmutableSet.<ConvertiblePair> of(new ConvertiblePair(GameSpecification.class, String.class), new ConvertiblePair(String.class, GameSpecification.class));

    final private ObjectMapper objectMapper;

    public GameSpecificationConverter(ObjectMapper objectMapper) {
        this.objectMapper = checkNotNull(objectMapper);
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return TYPES;
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        // Step 1. Converting if source type is string
        try {
            if (source instanceof String) {
                // Step 1. Reading JSON string
                return objectMapper.readValue((String) source, GameSpecification.class);
            } else {
                // Step 2. Parsing JSON string
                return objectMapper.writeValueAsString(source);
            }
        } catch (IOException e) {
            return null;
        }
    }

}
