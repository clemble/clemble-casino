package com.clemble.casino.server.game.aspect.security;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.event.Event;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.PotGameContext;
import com.clemble.casino.game.event.server.GameEndedEvent;
import com.clemble.casino.game.event.server.GamePotEvent;
import com.clemble.casino.server.game.aspect.BasicGameAspect;

public class GamePotSecurityAspect extends BasicGameAspect<Event> {

    final private PotGameContext context;
    
    public GamePotSecurityAspect(PotGameContext context) {
        super(new EventTypeSelector(Event.class));
        this.context = context;
    }

    @Override
    public void doEvent(Event event) {
        if (event instanceof GamePotEvent)
            return;
        // Step 1. Going through the events
        if (!(event instanceof GameEndedEvent)) // TODO replace with Clemble errors
            throw new IllegalArgumentException();
        // Step 2. Checking game relates to the curently active game
        GameSessionKey sessionKey = ((GameEndedEvent<?>) event).getContext().getSession();
        if (!context.getCurrentSession().equals(sessionKey))
            throw new IllegalArgumentException();
    }

}
