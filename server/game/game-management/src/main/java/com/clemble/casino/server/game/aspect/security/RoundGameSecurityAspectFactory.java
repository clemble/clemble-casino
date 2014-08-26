package com.clemble.casino.server.game.aspect.security;

import com.clemble.casino.game.RoundGameContext;
import com.clemble.casino.game.configuration.RoundGameConfiguration;
import org.springframework.core.Ordered;

import com.clemble.casino.event.PlayerAwareEvent;
import com.clemble.casino.player.PlayerAwareUtils;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.RoundGameAspectFactory;

public class RoundGameSecurityAspectFactory implements RoundGameAspectFactory<PlayerAwareEvent> {

    @Override
    public GameAspect<PlayerAwareEvent> construct(RoundGameConfiguration configuration, RoundGameContext context) {
        return new RoundGameSecurityAspect(context, PlayerAwareUtils.toPlayerList(context.getPlayerContexts()));
    }

    @Override
    public int getOrder(){
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
