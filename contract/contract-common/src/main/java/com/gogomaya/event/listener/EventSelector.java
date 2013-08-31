package com.gogomaya.event.listener;

import com.gogomaya.event.Event;

public interface EventSelector {

    public boolean filter(Event event);

}
