package com.clemble.casino.schedule.spring;

import com.clemble.casino.server.spring.AbstractWebApplicationInitializer;

/**
 * Created by mavarazy on 11/8/14.
 */
public class ScheduleApplicationInitializer extends AbstractWebApplicationInitializer {

    public ScheduleApplicationInitializer() {
        super(ScheduleSpringConfiguration.class);
    }

}
