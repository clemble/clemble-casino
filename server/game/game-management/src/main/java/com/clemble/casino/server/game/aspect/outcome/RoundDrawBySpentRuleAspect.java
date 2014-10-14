package com.clemble.casino.server.game.aspect.outcome;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.lifecycle.management.event.GameEndedEvent;
import com.clemble.casino.game.lifecycle.management.event.RoundEndedEvent;
import com.clemble.casino.server.game.aspect.GameAspect;

public class RoundDrawBySpentRuleAspect extends GameAspect<RoundEndedEvent> {

    final public static RoundDrawBySpentRuleAspect INSTANCE = new RoundDrawBySpentRuleAspect();

    public RoundDrawBySpentRuleAspect() {
        super(new EventTypeSelector(RoundEndedEvent.class));
    }

    @Override
    public void doEvent(RoundEndedEvent event) {
        // Do nothing, nothing is expected from the client
    }

}
