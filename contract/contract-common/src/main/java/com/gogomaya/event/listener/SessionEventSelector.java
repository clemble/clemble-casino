package com.gogomaya.event.listener;

import com.gogomaya.event.Event;
import com.gogomaya.game.SessionAware;

public class SessionEventSelector implements EventSelector {

    final private long sessionId;

    public SessionEventSelector(long sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public boolean filter(Event event) {
        return event instanceof SessionAware ? ((SessionAware) event).getSession() == sessionId : false;
    }

}
