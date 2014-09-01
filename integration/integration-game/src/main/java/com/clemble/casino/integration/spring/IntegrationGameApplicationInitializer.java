package com.clemble.casino.integration.spring;

import com.clemble.casino.server.game.spring.GameManagementSpringConfiguration;
import com.clemble.casino.server.spring.AbstractWebApplicationInitializer;

/**
 * Created by mavarazy on 9/1/14.
 */
public class IntegrationGameApplicationInitializer extends AbstractWebApplicationInitializer {

    public IntegrationGameApplicationInitializer() {
        super(GameManagementSpringConfiguration.class);
    }

}
