package com.clemble.casino.server.game.aspect.security;

import com.clemble.casino.game.lifecycle.management.RoundGameContext;
import com.clemble.casino.game.lifecycle.configuration.RoundGameConfiguration;
import com.clemble.casino.game.lifecycle.management.RoundGameState;
import com.clemble.casino.player.event.PlayerEvent;
import org.springframework.core.Ordered;

import com.clemble.casino.player.PlayerAwareUtils;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.RoundGameAspectFactory;

public class RoundGameSecurityAspectFactory implements RoundGameAspectFactory<PlayerEvent> {

    @Override
    public GameAspect<PlayerEvent> construct(RoundGameConfiguration configuration, RoundGameState roundState) {
        return new RoundGameSecurityAspect(roundState.getContext());
    }

    @Override
    public int getOrder(){
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
