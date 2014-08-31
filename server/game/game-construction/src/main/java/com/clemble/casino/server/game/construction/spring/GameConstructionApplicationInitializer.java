package com.clemble.casino.server.game.construction.spring;

import com.clemble.casino.server.spring.AbstractWebApplicationInitializer;

/**
 * Created by mavarazy on 8/31/14.
 */
public class GameConstructionApplicationInitializer extends AbstractWebApplicationInitializer {

    public GameConstructionApplicationInitializer() {
        super(GameConstructionSpringConfiguration.class);
    }

}
