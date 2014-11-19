package com.clemble.casino.server.game.aspect.outcome;

import com.clemble.casino.client.event.EventSelectors;
import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.client.event.OutcomeTypeSelector;
import com.clemble.casino.game.lifecycle.management.event.GameEndedEvent;
import com.clemble.casino.game.lifecycle.management.event.RoundEndedEvent;
import com.clemble.casino.lifecycle.management.outcome.DrawOutcome;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.RoundGameAspect;

public class RoundDrawBySpentRuleAspect extends RoundGameAspect<RoundEndedEvent> {

    final public static RoundDrawBySpentRuleAspect INSTANCE = new RoundDrawBySpentRuleAspect();

    public RoundDrawBySpentRuleAspect() {
        super(EventSelectors.
            where(new EventTypeSelector(RoundEndedEvent.class)).
            and(new OutcomeTypeSelector<DrawOutcome>(DrawOutcome.class)));
    }

    @Override
    protected void doEvent(RoundEndedEvent event) {
        // Do nothing, nothing is expected from the client
    }

}
