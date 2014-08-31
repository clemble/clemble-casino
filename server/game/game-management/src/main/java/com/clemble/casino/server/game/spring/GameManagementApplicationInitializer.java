package com.clemble.casino.server.game.spring;

import com.clemble.casino.server.spring.AbstractWebApplicationInitializer;

/**
 * Created by mavarazy on 8/31/14.
 */
public class GameManagementApplicationInitializer extends AbstractWebApplicationInitializer {

    public GameManagementApplicationInitializer() {
        super(GameManagementSpringConfiguration.class);
    }

}
