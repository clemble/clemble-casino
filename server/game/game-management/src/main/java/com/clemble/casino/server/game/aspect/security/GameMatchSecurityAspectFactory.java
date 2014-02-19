package com.clemble.casino.server.game.aspect.security;

import org.springframework.core.Ordered;

import com.clemble.casino.event.PlayerAwareEvent;
import com.clemble.casino.game.MatchGameContext;
import com.clemble.casino.game.specification.MatchGameConfiguration;
import com.clemble.casino.player.PlayerAwareUtils;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.MatchGameAspectFactory;

public class GameMatchSecurityAspectFactory implements MatchGameAspectFactory<PlayerAwareEvent> {

    @Override
    public GameAspect<PlayerAwareEvent> construct(MatchGameConfiguration configuration, MatchGameContext context) {
        return new GameMatchSecurityAspect(PlayerAwareUtils.toPlayerList(context.getPlayerContexts()));
    }

    @Override
    public int getOrder(){
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
