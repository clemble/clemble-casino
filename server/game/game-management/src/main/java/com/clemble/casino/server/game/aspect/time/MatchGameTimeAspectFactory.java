package com.clemble.casino.server.game.aspect.time;

import com.clemble.casino.game.lifecycle.management.MatchGameContext;
import com.clemble.casino.game.lifecycle.management.event.RoundEndedEvent;
import com.clemble.casino.game.lifecycle.configuration.MatchGameConfiguration;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.MatchGameAspectFactory;

public class MatchGameTimeAspectFactory implements MatchGameAspectFactory<RoundEndedEvent> {

    @Override
    public GameAspect<RoundEndedEvent> construct(MatchGameConfiguration configuration, MatchGameContext potContext) {
        return new MatchGameTimeAspect(potContext);
    }

    @Override
    public int getOrder() {
        return 1;
    }

}
