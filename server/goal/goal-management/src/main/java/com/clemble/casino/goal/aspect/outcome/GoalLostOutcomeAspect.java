package com.clemble.casino.goal.aspect.outcome;

import com.clemble.casino.bet.PlayerBid;
import com.clemble.casino.bet.PlayerBidAware;
import com.clemble.casino.client.event.EventSelectors;
import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.client.event.OutcomeTypeSelector;
import com.clemble.casino.goal.GoalPaymentSource;
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

import java.util.Collection;
import java.util.Date;

/**
 * Created by mavarazy on 10/9/14.
 */
public class GoalLostOutcomeAspect
    extends GoalAspect<GoalEndedEvent>
    implements PlayerBidAware {

    final private Collection<PlayerBid> bids;
    final private SystemNotificationService systemNotificationService;

    public GoalLostOutcomeAspect(Collection<PlayerBid> bids, SystemNotificationService systemNotificationService) {
        super(EventSelectors.
            where(new EventTypeSelector(GoalEndedEvent.class)).
            and(new OutcomeTypeSelector(PlayerLostOutcome.class)));
        this.bids = bids;
        this.systemNotificationService = systemNotificationService;
    }

    @Override
    public Collection<PlayerBid> getBids() {
        return bids;
    }

    @Override
    protected void doEvent(GoalEndedEvent event) {
        // Step 1. Generating payment transaction
        // Account already balanced need to remove pending operation
        PaymentTransaction paymentTransaction = new PaymentTransaction().
            setTransactionKey(event.getBody().getGoalKey()).
            setTransactionDate(new Date()).
            setSource(new GoalPaymentSource(event.getBody().getGoalKey(), event.getOutcome()));
        // Step 3. Generating bid transaction
        for(PlayerBid playerBid: bids) {
            paymentTransaction.
                addOperation(new PaymentOperation(playerBid.getPlayer(), playerBid.getBid().getInterest(), Operation.Credit)).
                addOperation(new PaymentOperation(PlayerAware.DEFAULT_PLAYER, playerBid.getBid().getInterest(), Operation.Debit));
        }
        // Step 2. Processing payment transaction
        systemNotificationService.send(new SystemPaymentTransactionRequestEvent(paymentTransaction));
    }

}
