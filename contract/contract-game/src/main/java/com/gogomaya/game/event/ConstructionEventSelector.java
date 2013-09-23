package com.gogomaya.game.event;

import com.gogomaya.event.Event;
import com.gogomaya.event.GameConstructionEvent;
import com.gogomaya.event.listener.EventSelector;

public class ConstructionEventSelector implements EventSelector {

    final private long session;

    public ConstructionEventSelector(long sessionId) {
        this.session = sessionId;
    }

    @Override
    public boolean filter(Event event) {
        return event instanceof GameConstructionEvent && ((GameConstructionEvent) event).getSession() == session;
    }

}
