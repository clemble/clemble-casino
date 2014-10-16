package com.clemble.casino.goal.aspect.outcome;

import com.clemble.casino.client.event.EventSelectors;
import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.client.event.OutcomeTypeSelector;
import com.clemble.casino.goal.aspect.GoalAspect;
import com.clemble.casino.goal.lifecycle.management.event.GoalEndedEvent;
import com.clemble.casino.lifecycle.management.outcome.PlayerLostOutcome;
import com.clemble.casino.money.Money;
import com.clemble.casino.money.Operation;
import com.clemble.casino.payment.PaymentOperation;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.server.event.payment.SystemPaymentTransactionRequestEvent;
import com.clemble.casino.server.player.notification.SystemNotificationService;

import java.util.Date;

/**
 * Created by mavarazy on 10/9/14.
 */
public class GoalLostOutcomeAspect extends GoalAspect<GoalEndedEvent> {

    final private String player;
    final private Money bidTotal;
    final private SystemNotificationService systemNotificationService;

    public GoalLostOutcomeAspect(String player, Money bidTotal, SystemNotificationService systemNotificationService) {
        super(EventSelectors.
            where(new EventTypeSelector(GoalEndedEvent.class)).
            and(new OutcomeTypeSelector(PlayerLostOutcome.class)));
        this.player = player;
        this.bidTotal = bidTotal;
        this.systemNotificationService = systemNotificationService;
    }

    @Override
    protected void doEvent(GoalEndedEvent event) {
        // Step 1. Generating payment transaction
        // Account already balanced need to remove pending operation
        PaymentTransaction paymentTransaction = new PaymentTransaction().
            setTransactionKey(event.getGoalKey()).
            setTransactionDate(new Date()).
                addOperation(new PaymentOperation(player, Money.create(bidTotal.getCurrency(), 0), Operation.Credit)).
                addOperation(new PaymentOperation(PlayerAware.DEFAULT_PLAYER, Money.create(bidTotal.getCurrency(), 0), Operation.Debit));
        // Step 2. Processing payment transaction
        systemNotificationService.send(new SystemPaymentTransactionRequestEvent(paymentTransaction));
    }

}

