package com.clemble.casino.server.game.aspect.presence;

import com.clemble.casino.game.RoundGameContext;
import com.clemble.casino.game.event.RoundEndedEvent;
import com.clemble.casino.game.configuration.RoundGameConfiguration;
import org.springframework.core.Ordered;

import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.RoundGameAspectFactory;
import com.clemble.casino.server.player.presence.ServerPlayerPresenceService;

public class GameEndPresenceAspectFactory implements RoundGameAspectFactory<RoundEndedEvent> {

    final private GameEndPresenceAspect endPresenceAspect;

    public GameEndPresenceAspectFactory(ServerPlayerPresenceService presenceService) {
        this.endPresenceAspect = new GameEndPresenceAspect(presenceService);
    }

    @Override
    public GameAspect<RoundEndedEvent> construct(RoundGameConfiguration configuration, RoundGameContext context) {
        return endPresenceAspect;
    }

    @Override
    public int getOrder(){
        return Ordered.LOWEST_PRECEDENCE;
    }

}
