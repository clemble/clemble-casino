package com.clemble.casino.server.game.aspect.security;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.event.Event;
import com.clemble.casino.game.MatchGameContext;
import com.clemble.casino.game.event.server.GameEndedEvent;
import com.clemble.casino.game.event.server.MatchEvent;
import com.clemble.casino.server.game.aspect.GameAspect;

public class MatchGameSecurityAspect extends GameAspect<Event> {

    final private MatchGameContext context;
    
    public MatchGameSecurityAspect(MatchGameContext context) {
        super(new EventTypeSelector(Event.class));
        this.context = context;
    }

    @Override
    public void doEvent(Event event) {
        if (event instanceof MatchEvent)
            return;
        // Step 1. Going through the events
        if (!(event instanceof GameEndedEvent)) // TODO replace with Clemble errors
            throw new IllegalArgumentException();
        // Step 2. Checking game relates to the curently active game
        String sessionKey = ((GameEndedEvent<?>) event).getContext().getSessionKey();
        if (!context.getCurrentSession().equals(sessionKey))
            throw new IllegalArgumentException();
    }

}
