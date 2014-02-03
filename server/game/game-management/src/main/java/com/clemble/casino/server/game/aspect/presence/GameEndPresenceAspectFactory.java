package com.clemble.casino.server.game.aspect.presence;

import org.springframework.core.Ordered;

import com.clemble.casino.game.MatchGameContext;
import com.clemble.casino.game.event.server.GameMatchEndedEvent;
import com.clemble.casino.game.specification.MatchGameConfiguration;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.MatchGameAspectFactory;
import com.clemble.casino.server.player.presence.ServerPlayerPresenceService;

public class GameEndPresenceAspectFactory implements MatchGameAspectFactory<GameMatchEndedEvent<?>> {

    final private GameEndPresenceAspect endPresenceAspect;

    public GameEndPresenceAspectFactory(ServerPlayerPresenceService presenceService) {
        this.endPresenceAspect = new GameEndPresenceAspect(presenceService);
    }

    @Override
    public GameAspect<GameMatchEndedEvent<?>> construct(MatchGameConfiguration configuration, MatchGameContext context) {
        return endPresenceAspect;
    }

    @Override
    public int getOrder(){
        return Ordered.LOWEST_PRECEDENCE;
    }

}
