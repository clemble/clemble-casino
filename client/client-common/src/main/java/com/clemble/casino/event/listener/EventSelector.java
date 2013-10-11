package com.clemble.casino.event.listener;

import com.clemble.casino.event.Event;

public interface EventSelector {

    public boolean filter(Event event);

}
