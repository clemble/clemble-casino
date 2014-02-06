package com.clemble.casino.server.game.aspect.pot;

import com.clemble.casino.game.PotGameContext;
import com.clemble.casino.game.event.server.GameMatchEndedEvent;
import com.clemble.casino.game.specification.PotGameConfiguration;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.PotGameAspectFactory;

public class PotFillAspectFactory implements PotGameAspectFactory<GameMatchEndedEvent> {

    @Override
    public GameAspect<GameMatchEndedEvent> construct(PotGameConfiguration configuration, PotGameContext context) {
        // Step 1. Checking initiation
        switch (configuration.getPotFillRule()) {
        case maxcommon:
            return new PotFillMaxCommonAspect(context);
        case reminder:
            return new PotFillReminderAspect(context);
        case none:
            return new PotFillNoneAspect(context);
        default:
            throw new IllegalArgumentException();
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }

}
