package com.clemble.casino.client.event;

import com.clemble.casino.event.ConstructionEvent;
import com.clemble.casino.event.Event;
import com.clemble.casino.game.GameSessionAware;
import com.clemble.casino.game.GameSessionKey;

public class ConstructionEventSelector implements EventSelector, GameSessionAware {

    /**
     * Generated 29/11/13
     */
    private static final long serialVersionUID = 4072930365311184802L;

    final private GameSessionKey sessionKey;

    public ConstructionEventSelector(GameSessionKey sessionKey) {
        this.sessionKey = sessionKey;
    }

    @Override
    public boolean filter(Event event) {
        return event instanceof ConstructionEvent && ((ConstructionEvent) event).getSession().equals(sessionKey);
    }

    @Override
    public GameSessionKey getSession() {
        return sessionKey;
    }

}
