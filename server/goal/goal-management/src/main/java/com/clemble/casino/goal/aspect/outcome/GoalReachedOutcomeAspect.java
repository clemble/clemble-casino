package com.clemble.casino.goal.aspect.outcome;

import com.clemble.casino.client.event.EventSelector;
import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.lifecycle.management.GamePlayerAccount;
import com.clemble.casino.game.lifecycle.management.GamePlayerContext;
import com.clemble.casino.goal.aspect.GoalAspect;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.management.event.GoalEndedEvent;
import com.clemble.casino.goal.lifecycle.management.event.GoalReachedEvent;
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
public class GoalReachedOutcomeAspect extends GoalAspect<GoalReachedEvent> {

    final private String player;
    final private Money bidTotal;
    final private SystemNotificationService systemNotificationService;

    public GoalReachedOutcomeAspect(String player, Money bidTotal, SystemNotificationService systemNotificationService) {
        super(new EventTypeSelector(GoalReachedEvent.class));
        this.player = player;
        this.bidTotal = bidTotal;
        this.systemNotificationService = systemNotificationService;
    }

    @Override
    public void doEvent(GoalReachedEvent event) {
        // Step 1. Generating payment transaction
        PaymentTransaction paymentTransaction = new PaymentTransaction()
            .setTransactionKey(event.getGoalKey())
            .setTransactionDate(new Date())
            .addPaymentOperation(new PaymentOperation(player, bidTotal, Operation.Debit))
            .addPaymentOperation(new PaymentOperation(PlayerAware.DEFAULT_PLAYER, bidTotal, Operation.Credit));
        // Step 2. Processing payment transaction
        systemNotificationService.notify(new SystemPaymentTransactionRequestEvent(paymentTransaction));
    }
}
