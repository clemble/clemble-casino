package com.clemble.casino.goal.spring;

import com.clemble.casino.server.spring.AbstractWebApplicationInitializer;

/**
 * Created by mavarazy on 9/20/14.
 */
public class GoalManagerApplicationInitializer extends AbstractWebApplicationInitializer {

    public GoalManagerApplicationInitializer() {
        super(GoalManagementSpringConfiguration.class);
    }

}
