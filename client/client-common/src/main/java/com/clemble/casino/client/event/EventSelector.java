package com.clemble.casino.client.event;

import com.clemble.casino.event.Event;

public interface EventSelector {

    public boolean filter(Event event);

}
