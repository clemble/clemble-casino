package com.clemble.casino.server.game.aspect.outcome;

import java.util.Date;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.event.server.GameEndedEvent;
import com.clemble.casino.game.outcome.DrawOutcome;
import com.clemble.casino.game.outcome.GameOutcome;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.server.game.aspect.BasicGameAspect;

public class DrawBySpentRuleAspect extends BasicGameAspect<GameEndedEvent<?>> {

    final public static DrawBySpentRuleAspect INSTANCE = new DrawBySpentRuleAspect();

    public DrawBySpentRuleAspect() {
        super(new EventTypeSelector(GameEndedEvent.class));
    }

    @Override
    public void doEvent(GameEndedEvent<?> event) {
        GameOutcome outcome = event.getOutcome();
        if (outcome instanceof DrawOutcome) {
            // Step 1. Generating payment transaction
            PaymentTransaction paymentTransaction = new PaymentTransaction()
                .setTransactionKey(event.getSession().toPaymentTransactionKey())
                .setTransactionDate(new Date());
            // Step 2. Specifying payment transaction
            event.setTransaction(paymentTransaction);
        }
    }

}