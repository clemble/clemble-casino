package com.gogomaya.event.listener;

import com.gogomaya.event.Event;
import com.gogomaya.game.GameSessionKey;
import com.gogomaya.game.SessionAware;

public class SessionEventSelector implements EventSelector {

    final private GameSessionKey session;

    public SessionEventSelector(GameSessionKey sessionId) {
        this.session = sessionId;
    }

    @Override
    public boolean filter(Event event) {
        return event instanceof SessionAware ? ((SessionAware) event).getSession().equals(session) : false;
    }

}
