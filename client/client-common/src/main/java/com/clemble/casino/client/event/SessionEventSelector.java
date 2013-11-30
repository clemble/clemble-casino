package com.clemble.casino.client.event;

import com.clemble.casino.event.Event;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameSessionAware;

public class SessionEventSelector implements EventSelector {

    final private GameSessionKey session;

    public SessionEventSelector(GameSessionKey sessionId) {
        this.session = sessionId;
    }

    @Override
    public boolean filter(Event event) {
        return event instanceof GameSessionAware ? ((GameSessionAware) event).getSession().equals(session) : false;
    }

}
