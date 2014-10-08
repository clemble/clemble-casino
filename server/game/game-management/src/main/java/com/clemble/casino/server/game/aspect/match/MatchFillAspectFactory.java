package com.clemble.casino.server.game.aspect.match;

import com.clemble.casino.game.lifecycle.management.MatchGameContext;
import com.clemble.casino.game.lifecycle.management.event.RoundEndedEvent;
import com.clemble.casino.game.lifecycle.configuration.MatchGameConfiguration;
import com.clemble.casino.server.game.action.MatchGameState;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.MatchGameAspectFactory;

public class MatchFillAspectFactory implements MatchGameAspectFactory<RoundEndedEvent> {

    @Override
    public GameAspect<RoundEndedEvent> construct(MatchGameConfiguration configuration, MatchGameState matchState) {
        // Step 1. Checking initiation
        switch (configuration.getMatchFillRule()) {
        case maxcommon:
            return new MatchFillMaxCommonAspect(matchState.getContext());
        case reminder:
            return new MatchFillReminderAspect(matchState.getContext());
        case none:
            return new MatchFillNoneAspect(matchState.getContext());
        default:
            throw new IllegalArgumentException();
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }

}
