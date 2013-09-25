package com.gogomaya.event.listener;

import com.gogomaya.event.Event;
import com.gogomaya.event.GameConstructionEvent;
import com.gogomaya.game.GameSessionKey;

public class ConstructionEventSelector implements EventSelector {

    final private GameSessionKey session;

    public ConstructionEventSelector(GameSessionKey sessionId) {
        this.session = sessionId;
    }

    @Override
    public boolean filter(Event event) {
        return event instanceof GameConstructionEvent && ((GameConstructionEvent) event).getSession().equals(session);
    }

}
