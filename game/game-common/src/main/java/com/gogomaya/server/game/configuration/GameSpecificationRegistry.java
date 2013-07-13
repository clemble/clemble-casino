package com.gogomaya.server.game.configuration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.google.common.collect.ImmutableMap;

public class GameSpecificationRegistry implements ApplicationContextAware {

    private Map<String, GameSpecificationConfiguration> gameToSpecifications = new HashMap<>();

    public GameSpecificationOptions getSpecificationOptions(String name) {
        return gameToSpecifications.get(name).getSpecificationOptions();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // Step 1. Fetching specification configurations
        Map<String, GameSpecificationConfiguration> specificationConfigurations = applicationContext.getBeansOfType(GameSpecificationConfiguration.class);
        // Step 2. Checking validity
        Map<String, GameSpecificationConfiguration> gameToSpec = new HashMap<>();
        for (GameSpecificationConfiguration configuration : specificationConfigurations.values()) {
            if (gameToSpec.containsKey(configuration.getName()))
                throw new IllegalArgumentException("Can't have non unique configuration for SpecificationConfigurations " + configuration + " "
                        + gameToSpec.get(configuration.getName()));
            gameToSpec.put(configuration.getName(), configuration);
        }
        // Step 3. Updating specification references
        gameToSpecifications = ImmutableMap.<String, GameSpecificationConfiguration> copyOf(gameToSpec);
    }
}
