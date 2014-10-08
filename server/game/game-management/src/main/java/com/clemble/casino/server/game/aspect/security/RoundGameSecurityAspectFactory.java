package com.clemble.casino.server.game.aspect.security;

import com.clemble.casino.event.Event;
import com.clemble.casino.game.lifecycle.management.RoundGameContext;
import com.clemble.casino.game.lifecycle.configuration.RoundGameConfiguration;
import com.clemble.casino.game.lifecycle.management.RoundGameState;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.player.event.PlayerEvent;
import org.springframework.core.Ordered;

import com.clemble.casino.player.PlayerAwareUtils;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.RoundGameAspectFactory;

public class RoundGameSecurityAspectFactory<T extends Event & PlayerAware> implements RoundGameAspectFactory<T> {

    @Override
    public GameAspect<T> construct(RoundGameConfiguration configuration, RoundGameState roundState) {
        return new RoundGameSecurityAspect<T>(roundState.getContext());
    }

    @Override
    public int getOrder(){
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
