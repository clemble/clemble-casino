package com.clemble.casino.server.connection.spring;

import com.clemble.casino.server.spring.AbstractWebApplicationInitializer;

/**
 * Created by mavarazy on 7/4/14.
 */
public class PlayerConnectionApplicationInitializer extends AbstractWebApplicationInitializer {

    public PlayerConnectionApplicationInitializer() {
        super(PlayerConnectionWebSpringConfiguration.class);
    }

}
