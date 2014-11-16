package com.clemble.casino.server.aspect;

import com.clemble.casino.event.Event;
import com.clemble.casino.lifecycle.configuration.Configuration;
import com.clemble.casino.lifecycle.management.State;

/**
 * Created by mavarazy on 11/16/14.
 */
public interface GenericClembleAspectFactory<T extends Event, C extends Configuration, S extends State> extends ClembleAspectFactory<T, C, S> {
}
