package com.clemble.casino.goal.configuration.spring;

import com.clemble.casino.server.spring.AbstractWebApplicationInitializer;

/**
 * Created by mavarazy on 9/15/14.
 */
public class GoalConfigurationApplicationInitializer extends AbstractWebApplicationInitializer {

    public GoalConfigurationApplicationInitializer() {
        super(GoalConfigurationSpringConfiguration.class);
    }

}
