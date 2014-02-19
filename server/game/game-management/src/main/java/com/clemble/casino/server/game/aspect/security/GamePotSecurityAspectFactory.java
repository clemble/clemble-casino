package com.clemble.casino.server.game.aspect.security;

import org.springframework.core.Ordered;

import com.clemble.casino.event.Event;
import com.clemble.casino.game.PotGameContext;
import com.clemble.casino.game.specification.PotGameConfiguration;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.PotGameAspectFactory;

public class GamePotSecurityAspectFactory implements PotGameAspectFactory<Event>{

    @Override
    public GameAspect<Event> construct(PotGameConfiguration initiation, PotGameContext potContext) {
        return new GamePotSecurityAspect(potContext);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
