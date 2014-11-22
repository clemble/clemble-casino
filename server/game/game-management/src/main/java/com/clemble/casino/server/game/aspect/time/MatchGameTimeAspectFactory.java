package com.clemble.casino.server.game.aspect.time;

import com.clemble.casino.game.lifecycle.management.event.RoundEndedEvent;
import com.clemble.casino.game.lifecycle.configuration.MatchGameConfiguration;
import com.clemble.casino.game.lifecycle.management.MatchGameState;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.MatchGameAspectFactory;
import org.springframework.core.Ordered;

public class MatchGameTimeAspectFactory implements MatchGameAspectFactory<RoundEndedEvent> {

    @Override
    public GameAspect<RoundEndedEvent> construct(MatchGameConfiguration configuration, MatchGameState matchState) {
        return new MatchGameTimeAspect(matchState.getContext());
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }

}
