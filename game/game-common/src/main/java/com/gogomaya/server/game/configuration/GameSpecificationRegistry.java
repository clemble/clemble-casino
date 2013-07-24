package com.gogomaya.server.game.configuration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.gogomaya.server.game.Game;
import com.google.common.collect.ImmutableMap;

public class GameSpecificationRegistry implements ApplicationContextAware {

    private Map<Game, GameSpecificationConfiguration> gameToSpecifications = new HashMap<>();

    public GameSpecificationOptions getSpecificationOptions(Game game) {
        return gameToSpecifications.get(game).getSpecificationOptions();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // Step 1. Fetching specification configurations
        Map<String, GameSpecificationConfiguration> specificationConfigurations = applicationContext.getBeansOfType(GameSpecificationConfiguration.class);
        // Step 2. Checking validity
        Map<Game, GameSpecificationConfiguration> gameToSpec = new HashMap<>();
        for (GameSpecificationConfiguration configuration : specificationConfigurations.values()) {
            if (gameToSpec.containsKey(configuration.getGame()))
                throw new IllegalArgumentException("Can't have non unique configuration for SpecificationConfigurations " + configuration + " "
                        + gameToSpec.get(configuration.getGame()));
            gameToSpec.put(configuration.getGame(), configuration);
        }
        // Step 3. Updating specification references
        gameToSpecifications = ImmutableMap.<Game, GameSpecificationConfiguration> copyOf(gameToSpec);
    }
}
