package com.clemble.casino.server.game.aspect.presence;

import org.springframework.core.Ordered;

import com.clemble.casino.game.construct.ServerGameInitiation;
import com.clemble.casino.game.event.server.GameEndedEvent;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.GameAspectFactory;
import com.clemble.casino.server.player.presence.ServerPlayerPresenceService;

public class GameEndPresenceAspectFactory implements GameAspectFactory<GameEndedEvent<?>> {

    final private GameEndPresenceAspect endPresenceAspect;

    public GameEndPresenceAspectFactory(ServerPlayerPresenceService presenceService) {
        this.endPresenceAspect = new GameEndPresenceAspect(presenceService);
    }

    @Override
    public GameAspect<GameEndedEvent<?>> construct(ServerGameInitiation initiation) {
        return endPresenceAspect;
    }

    @Override
    public int getOrder(){
        return Ordered.LOWEST_PRECEDENCE;
    }

}
