package com.clemble.casino.client.event;

import com.clemble.casino.event.ConstructionEvent;
import com.clemble.casino.event.Event;
import com.clemble.casino.game.GameSessionKey;

public class ConstructionEventSelector implements EventSelector {

    final private GameSessionKey session;

    public ConstructionEventSelector(GameSessionKey sessionId) {
        this.session = sessionId;
    }

    @Override
    public boolean filter(Event event) {
        return event instanceof ConstructionEvent && ((ConstructionEvent) event).getSession().equals(session);
    }

}
