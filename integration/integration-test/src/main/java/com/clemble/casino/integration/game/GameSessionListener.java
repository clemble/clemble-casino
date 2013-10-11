package com.clemble.casino.integration.game;

import com.clemble.casino.event.Event;
import com.clemble.casino.game.SessionAware;

abstract public class GameSessionListener {

    public GameSessionListener() {
    }

    final public void update(Event event) {
        // Step 1. Generic sanity check of SessionAware
        if (!(event instanceof SessionAware))
            throw new IllegalArgumentException("Event must be SessionAware only");
        // Step 2. Generic check of session value
        notify(event);
    }

    abstract protected void notify(Event event);

}
