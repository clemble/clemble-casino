package com.clemble.casino.server.game.aspect.next;

import com.clemble.casino.game.lifecycle.configuration.MatchGameConfiguration;
import com.clemble.casino.game.lifecycle.management.MatchGameState;
import com.clemble.casino.game.lifecycle.management.event.MatchChangedEvent;
import com.clemble.casino.game.lifecycle.management.event.MatchEvent;
import com.clemble.casino.server.game.action.GameManagerFactoryFacade;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.MatchGameAspectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;

/**
 * Created by mavarazy on 14/10/14.
 */
public class MatchNextGameAspectFactory implements MatchGameAspectFactory<MatchEvent> {

    final private Logger LOG = LoggerFactory.getLogger(NextGameAspectFactory.class);

    final private GameManagerFactoryFacade managerFactory;

    public MatchNextGameAspectFactory(GameManagerFactoryFacade managerFactory) {
        this.managerFactory = managerFactory;
    }

    @Override
    public GameAspect<MatchEvent> construct(MatchGameConfiguration configuration, MatchGameState state) {
        LOG.debug("{} has a parent, constructing next game aspect", state.getContext().getSessionKey());
        return new MatchNextGameAspect(managerFactory);
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE - 7;
    }

}
