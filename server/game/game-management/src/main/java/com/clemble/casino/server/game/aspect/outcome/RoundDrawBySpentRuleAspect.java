package com.clemble.casino.server.game.aspect.outcome;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.lifecycle.management.event.GameEndedEvent;
import com.clemble.casino.server.game.aspect.GameAspect;

public class RoundDrawBySpentRuleAspect extends GameAspect<GameEndedEvent<?>> {

    final public static RoundDrawBySpentRuleAspect INSTANCE = new RoundDrawBySpentRuleAspect();

    public RoundDrawBySpentRuleAspect() {
        super(new EventTypeSelector(GameEndedEvent.class));
    }

    @Override
    public void doEvent(GameEndedEvent<?> event) {
        // Do nothing, nothing is expected from the client
    }

}
