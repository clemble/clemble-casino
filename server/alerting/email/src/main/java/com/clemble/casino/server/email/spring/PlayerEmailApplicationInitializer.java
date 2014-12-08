package com.clemble.casino.server.email.spring;

import com.clemble.casino.server.spring.AbstractWebApplicationInitializer;

/**
 * Created by mavarazy on 12/6/14.
 */
public class PlayerEmailApplicationInitializer extends AbstractWebApplicationInitializer {

    public PlayerEmailApplicationInitializer() {
        super(PlayerEmailSpringConfiguration.class);
    }

}
