package com.clemble.casino.server.game.aspect.pot;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.event.server.GameEndedEvent;
import com.clemble.casino.game.outcome.PlayerWonOutcome;
import com.clemble.casino.server.game.aspect.BasicGameAspect;

public class PotRuleAscpect extends BasicGameAspect<GameEndedEvent<?>>{

    public PotRuleAscpect() {
        super(new EventTypeSelector(GameEndedEvent.class));
    }

    @Override
    public void doEvent(GameEndedEvent<?> event) {
        // Step 1. Checking this is won outcome, others are ignored
        if (event.getOutcome() instanceof PlayerWonOutcome) {
            
        }
    }

}
