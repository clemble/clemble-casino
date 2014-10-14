package com.clemble.casino.server.game.aspect.security;

import com.clemble.casino.game.lifecycle.configuration.MatchGameConfiguration;
import com.clemble.casino.game.lifecycle.management.MatchGameState;
import org.springframework.core.Ordered;

import com.clemble.casino.event.Event;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.MatchGameAspectFactory;

public class MatchGameSecurityAspectFactory implements MatchGameAspectFactory<Event> {

    @Override
    public GameAspect<Event> construct(MatchGameConfiguration initiation, MatchGameState matchState) {
        return new MatchGameSecurityAspect(matchState.getContext());
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
