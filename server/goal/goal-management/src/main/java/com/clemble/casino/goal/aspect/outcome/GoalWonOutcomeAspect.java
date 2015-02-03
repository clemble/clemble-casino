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
import com.clemble.casino.lifecycle.management.outcome.PlayerWonOutcome;
import com.clemble.casino.money.Currency;
import com.clemble.casino.money.Money;
import com.clemble.casino.money.Operation;
import com.clemble.casino.payment.PaymentOperation;
import com.clemble.casino.payment.PaymentTransaction;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.server.event.payment.SystemPaymentTransactionRequestEvent;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.Collection;

/**
 * Created by mavarazy on 10/9/14.
 */
public class GoalWonOutcomeAspect
    extends GoalAspect<GoalEndedEvent> {

    final private SystemNotificationService systemNotificationService;

    public GoalWonOutcomeAspect(SystemNotificationService systemNotificationService) {
        super(EventSelectors.
                where(new EventTypeSelector(GoalEndedEvent.class)).
                and(new OutcomeTypeSelector(PlayerWonOutcome.class)));
        this.systemNotificationService = systemNotificationService;
    }

    @Override
    protected void doEvent(GoalEndedEvent event) {
        GoalState state = event.getBody();
        Collection<PlayerBet> bets = event.getBody().getBank().getBets();
        // Step 1. Generating payment transaction
        PaymentTransaction paymentTransaction = new PaymentTransaction()
            .setTransactionKey(event.getBody().getGoalKey())
            .setTransactionDate(DateTime.now(DateTimeZone.UTC))
            .setSource(new GoalPaymentSource(event.getBody().getGoalKey(), event.getOutcome()));
        // Step 2. Processing payment transaction
        for(PlayerBet playerBid: bets) {
            paymentTransaction.
                addOperation(new PaymentOperation(playerBid.getPlayer(), playerBid.getBet().getInterest(), Operation.Debit)).
                addOperation(new PaymentOperation(playerBid.getPlayer(), playerBid.getBet().getAmount(), Operation.Credit)).
                addOperation(new PaymentOperation(PlayerAware.DEFAULT_PLAYER, playerBid.getBet().getAmount(), Operation.Debit)).
                addOperation(new PaymentOperation(PlayerAware.DEFAULT_PLAYER, playerBid.getBet().getInterest(), Operation.Credit));
        }
        // Step 3. Checking value
        systemNotificationService.send(new SystemPaymentTransactionRequestEvent(paymentTransaction));
    }
}
