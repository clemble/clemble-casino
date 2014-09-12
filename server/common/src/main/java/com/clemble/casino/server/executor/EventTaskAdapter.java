package com.clemble.casino.server.executor;

import com.clemble.casino.event.Event;

import java.util.Collection;

/**
 * Created by mavarazy on 9/9/14.
 */
public interface EventTaskAdapter {

    void process(EventTask task);

}
