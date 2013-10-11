package com.clemble.casino.event.listener;

import com.clemble.casino.event.Event;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.SessionAware;

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
