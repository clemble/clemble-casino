package com.clemble.casino.server.goal.spring;

import com.clemble.casino.server.spring.AbstractWebApplicationInitializer;

/**
 * Created by mavarazy on 8/2/14.
 */
public class GoalApplicationInitializer extends AbstractWebApplicationInitializer {

    public GoalApplicationInitializer() {
        super(GoalSpringConfiguration.class);
    }

}
