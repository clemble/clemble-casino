package com.clemble.casino.server.converters;

import java.util.Set;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.configuration.GameConfigurationKey;
import com.google.common.collect.ImmutableSet;

public class GameConfigurationKeyConverter implements GenericConverter {

    final private Set<ConvertiblePair> TYPES = ImmutableSet.<ConvertiblePair> of(new ConvertiblePair(GameConfigurationKey.class, String.class), new ConvertiblePair(String.class, GameConfigurationKey.class));

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
            return new GameConfigurationKey(Game.valueOf(parts[0]), parts[1]);
        } else {
            GameConfigurationKey configurationKey = (GameConfigurationKey) source;
            return configurationKey.getGame() + ":" + configurationKey.getSpecificationName();
        }
    }

}
