package com.clemble.casino.server.game.aspect.outcome;

import java.util.Date;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.event.server.GameEndedEvent;
import com.clemble.casino.game.outcome.DrawOutcome;
import com.clemble.casino.game.outcome.GameOutcome;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.server.game.aspect.BasicGameAspect;

public class RoundDrawBySpentRuleAspect extends BasicGameAspect<GameEndedEvent<?>> {

    final public static RoundDrawBySpentRuleAspect INSTANCE = new RoundDrawBySpentRuleAspect();

    public RoundDrawBySpentRuleAspect() {
        super(new EventTypeSelector(GameEndedEvent.class));
    }

    @Override
    public void doEvent(GameEndedEvent<?> event) {
        // Do nothing, nothing is expected from the client
    }

}