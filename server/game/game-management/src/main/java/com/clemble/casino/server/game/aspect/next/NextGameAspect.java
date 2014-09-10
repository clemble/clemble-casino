package com.clemble.casino.server.game.aspect.next;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.event.GameEndedEvent;
import com.clemble.casino.server.game.action.GameManagerFactory;
import com.clemble.casino.server.game.aspect.GameAspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NextGameAspect extends GameAspect<GameEndedEvent<?>> {

    final private static Logger LOG = LoggerFactory.getLogger(NextGameAspect.class);

    final private GameContext<?> context;
    final private GameManagerFactory managerFactory;

    public NextGameAspect(GameContext<?> context, GameManagerFactory managerFactory) {
        super(new EventTypeSelector(GameEndedEvent.class));
        this.context = context;
        this.managerFactory = managerFactory;
    }

    @Override
    public void doEvent(GameEndedEvent<?> endedEvent) {
        // Step 1. Fetching Parent session key
        String sessionKey = context.getParent().getSessionKey();
        LOG.debug("{} ended next {}", context.getSessionKey(), sessionKey);
        // Step 2. Notifying parent of child game ended
        LOG.debug("{} forwarding end event for {}", context.getSessionKey(), sessionKey);
        managerFactory.get(sessionKey).process(endedEvent);
    }

}
