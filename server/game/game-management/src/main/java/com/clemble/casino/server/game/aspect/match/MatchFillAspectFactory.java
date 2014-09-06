package com.clemble.casino.server.game.aspect.match;

import com.clemble.casino.game.MatchGameContext;
import com.clemble.casino.game.event.server.RoundEndedEvent;
import com.clemble.casino.game.configuration.MatchGameConfiguration;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.MatchGameAspectFactory;

public class MatchFillAspectFactory implements MatchGameAspectFactory<RoundEndedEvent> {

    @Override
    public GameAspect<RoundEndedEvent> construct(MatchGameConfiguration configuration, MatchGameContext context) {
        // Step 1. Checking initiation
        switch (configuration.getMatchFillRule()) {
        case maxcommon:
            return new MatchFillMaxCommonAspect(context);
        case reminder:
            return new MatchFillReminderAspect(context);
        case none:
            return new MatchFillNoneAspect(context);
        default:
            throw new IllegalArgumentException();
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }

}
