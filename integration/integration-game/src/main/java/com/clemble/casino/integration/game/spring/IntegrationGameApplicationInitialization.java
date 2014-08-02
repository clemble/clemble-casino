package com.clemble.casino.integration.game.spring;

import com.clemble.casino.server.spring.AbstractWebApplicationInitializer;

public class IntegrationGameApplicationInitialization extends AbstractWebApplicationInitializer {

    public IntegrationGameApplicationInitialization() {
        super(IntegrationGameWebSpringConfiguration.class);
    }

}
