package com.gogomaya.event.listener;

import com.gogomaya.event.Event;

public class ConstructionEventSelector implements EventSelector {

    final private long sessionId;

    public ConstructionEventSelector(long sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public boolean filter(Event event) {
        return event instanceof GameCon;
    }

}
