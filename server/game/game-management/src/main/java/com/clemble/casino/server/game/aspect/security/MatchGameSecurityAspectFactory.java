package com.clemble.casino.server.game.aspect.security;

import com.clemble.casino.game.MatchGameContext;
import com.clemble.casino.game.configuration.MatchGameConfiguration;
import org.springframework.core.Ordered;

import com.clemble.casino.event.Event;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.MatchGameAspectFactory;

public class MatchGameSecurityAspectFactory implements MatchGameAspectFactory<Event> {

    @Override
    public GameAspect<Event> construct(MatchGameConfiguration initiation, MatchGameContext potContext) {
        return new MatchGameSecurityAspect(potContext);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
