package com.clemble.casino.server.game.aspect.next;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.event.server.GameEndedEvent;
import com.clemble.casino.server.game.action.GameManagerFactory;
import com.clemble.casino.server.game.aspect.BasicGameAspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NextGameAspect extends BasicGameAspect<GameEndedEvent<?>> {

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
        GameSessionKey sessionKey = context.getParent().getSession();
        LOG.debug("{} ended next {}", context.getSession(), sessionKey);
        // Step 2. Notifying parent of child game ended
        LOG.debug("{} forwarding end event for {}", context.getSession(), sessionKey);
        managerFactory.get(sessionKey).process(endedEvent);
    }

}
