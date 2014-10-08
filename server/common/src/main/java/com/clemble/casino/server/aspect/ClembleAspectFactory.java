package com.clemble.casino.server.aspect;

import com.clemble.casino.event.Event;
import com.clemble.casino.lifecycle.configuration.Configuration;
import com.clemble.casino.lifecycle.management.State;
import org.springframework.core.PriorityOrdered;

/**
 * Created by mavarazy on 9/6/14.
 */
public interface ClembleAspectFactory<T extends Event, C extends Configuration, S extends State> extends PriorityOrdered {

    public ClembleAspect<T> construct(C configuration, S context);

}
