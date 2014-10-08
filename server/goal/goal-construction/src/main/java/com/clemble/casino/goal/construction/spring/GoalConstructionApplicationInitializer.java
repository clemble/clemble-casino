package com.clemble.casino.goal.construction.spring;

import com.clemble.casino.server.spring.AbstractWebApplicationInitializer;

/**
 * Created by mavarazy on 9/10/14.
 */
public class GoalConstructionApplicationInitializer extends AbstractWebApplicationInitializer {

    public GoalConstructionApplicationInitializer() {
        super(GoalConstructionSpringConfiguration.class);
    }

}
