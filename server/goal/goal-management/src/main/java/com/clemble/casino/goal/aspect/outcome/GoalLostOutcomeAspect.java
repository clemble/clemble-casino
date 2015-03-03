package com.clemble.casino.goal.aspect.outcome;

import com.clemble.casino.bet.PlayerBet;
import com.clemble.casino.bet.PlayerBetAware;
import com.clemble.casino.client.event.EventSelectors;
import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.client.event.OutcomeTypeSelector;
import com.clemble.casino.goal.GoalPaymentSource;
import com.clemble.casino.goal.aspect.GoalAspect;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.lifecycle.management.event.GoalEndedEvent;
import com.clemble.casino.lifecycle.management.outcome.PlayerLostOutcome;
import com.clemble.casino.money.Operation;
import com.clemble.casino.payment.PaymentOperation;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.server.event.payment.SystemPaymentTransactionRequestEvent;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import org.joda.time.DateTime;

import java.util.Collection;

/**
 * Created by mavarazy on 10/9/14.
 */
public class GoalLostOutcomeAspect
    extends GoalAspect<GoalEndedEvent> {

    final private SystemNotificationService systemNotificationService;

    public GoalLostOutcomeAspect(SystemNotificationService systemNotificationService) {
        super(EventSelectors.
            where(new EventTypeSelector(GoalEndedEvent.class)).
            and(new OutcomeTypeSelector(PlayerLostOutcome.class)));
        this.systemNotificationService = systemNotificationService;
    }


    @Override
    protected void doEvent(GoalEndedEvent event) {
        GoalState state = event.getBody();
        Collection<PlayerBet> bets = event.getBody().getBank().getBets();
        // Step 1. Generating payment transaction
        // Account already balanced need to remove pending operation
        PaymentTransaction paymentTransaction = new PaymentTransaction().
            setTransactionKey(event.getBody().getGoalKey()).
            setTransactionDate(DateTime.now()).
            setSource(GoalPaymentSource.create(event));
        // Step 3. Generating bid transaction
        for(PlayerBet playerBid: bets) {
            paymentTransaction.
                addOperation(new PaymentOperation(playerBid.getPlayer(), playerBid.getBet().getAmount(), Operation.Credit)).
                addOperation(new PaymentOperation(PlayerAware.DEFAULT_PLAYER, playerBid.getBet().getAmount(), Operation.Debit));
        }
        // Step 2. Processing payment transaction
        systemNotificationService.send(new SystemPaymentTransactionRequestEvent(paymentTransaction));
    }

}

