package com.clemble.casino.server.game.aspect.next;

import com.clemble.casino.game.lifecycle.management.GameState;
import com.clemble.casino.server.game.action.GameManagerFactoryFacade;
import com.clemble.casino.server.game.aspect.GenericGameAspectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;

import com.clemble.casino.game.lifecycle.management.GameContext;
import com.clemble.casino.game.lifecycle.management.event.GameEndedEvent;
import com.clemble.casino.game.lifecycle.configuration.GameConfiguration;
import com.clemble.casino.server.game.action.GameManagerFactory;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.GameAspectFactory;

public class NextGameAspectFactory implements GenericGameAspectFactory<GameEndedEvent<?>> {

    final private Logger LOG = LoggerFactory.getLogger(NextGameAspectFactory.class);

    final private GameManagerFactoryFacade managerFactory;

    public NextGameAspectFactory(GameManagerFactoryFacade managerFactory) {
        this.managerFactory = managerFactory;
    }

    @Override
    public GameAspect<GameEndedEvent<?>> construct(GameConfiguration configuration, GameState state) {
        if (state.getContext().getParent() != null) {
            LOG.debug("{} has a parent, constructing next game aspect", state.getContext().getSessionKey());
            return new NextGameAspect(state.getContext(), managerFactory);
        }
        LOG.debug("{} does not have a parent", state.getContext().getSessionKey());
        return null;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

}
