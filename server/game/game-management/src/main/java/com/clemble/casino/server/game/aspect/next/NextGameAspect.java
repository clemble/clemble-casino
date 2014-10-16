package com.clemble.casino.server.game.aspect.next;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.lifecycle.management.GameContext;
import com.clemble.casino.game.lifecycle.management.event.GameEndedEvent;
import com.clemble.casino.server.game.action.GameManagerFactoryFacade;
import com.clemble.casino.server.game.aspect.GameAspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NextGameAspect extends GameAspect<GameEndedEvent> {

    final private static Logger LOG = LoggerFactory.getLogger(NextGameAspect.class);

    final private GameContext<?> context;
    final private GameManagerFactoryFacade managerFactory;

    public NextGameAspect(GameContext<?> context, GameManagerFactoryFacade managerFactory) {
        super(new EventTypeSelector(GameEndedEvent.class));
        this.context = context;
        this.managerFactory = managerFactory;
    }

    @Override
    protected void doEvent(GameEndedEvent endedEvent) {
        // Step 1. Fetching Parent session key
        String sessionKey = context.getParent().getSessionKey();
        LOG.debug("{} ended next {}", context.getSessionKey(), sessionKey);
        // Step 2. Notifying parent of child game ended
        LOG.debug("{} forwarding end event for {}", context.getSessionKey(), sessionKey);
        managerFactory.get(sessionKey).process(endedEvent);
    }

}
