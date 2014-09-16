package com.clemble.casino.server.game.configuration.spring;

import com.clemble.casino.server.spring.AbstractWebApplicationInitializer;

/**
 * Created by mavarazy on 8/30/14.
 */
public class GameConfigurationApplicationInitializer extends AbstractWebApplicationInitializer {

    public GameConfigurationApplicationInitializer(){
        super(GameConfigurationSpringConfiguration.class);
    }

}
