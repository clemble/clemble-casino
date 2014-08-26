package com.clemble.casino.server.game.aspect.next;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;

import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.event.server.GameEndedEvent;
import com.clemble.casino.game.configuration.GameConfiguration;
import com.clemble.casino.server.game.action.GameManagerFactory;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.GameAspectFactory;

public class NextGameAspectFactory implements GameAspectFactory<GameEndedEvent<?>, GameContext<?>, GameConfiguration> {

    final private Logger LOG = LoggerFactory.getLogger(NextGameAspectFactory.class);

    final private GameManagerFactory managerFactory;

    public NextGameAspectFactory(GameManagerFactory managerFactory) {
        this.managerFactory = managerFactory;
    }

    @Override
    public GameAspect<GameEndedEvent<?>> construct(GameConfiguration configuration, GameContext<?> context) {
        if (context.getParent() != null) {
            LOG.debug("{} has a parent, constructig next game aspect", context.getSessionKey());
            return new NextGameAspect(context, managerFactory);
        }
        LOG.debug("{} does not have a parent", context.getSessionKey());
        return null;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

}
