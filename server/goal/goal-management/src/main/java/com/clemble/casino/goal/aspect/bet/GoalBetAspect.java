package com.clemble.casino.goal.aspect.bet;

import com.clemble.casino.bet.PlayerBid;
import com.clemble.casino.client.PlayerActionTypeSelector;
import com.clemble.casino.client.event.EventSelector;
import com.clemble.casino.client.event.EventSelectors;
import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.event.Event;
import com.clemble.casino.goal.aspect.GoalAspect;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.management.event.GoalChangedBetEvent;
import com.clemble.casino.goal.lifecycle.management.event.GoalManagementEvent;
import com.clemble.casino.lifecycle.management.event.action.PlayerAction;
import com.clemble.casino.lifecycle.management.event.action.bet.BetAction;
import com.clemble.casino.money.Money;
import com.clemble.casino.money.Operation;
import com.clemble.casino.payment.PaymentOperation;
import com.clemble.casino.payment.PendingTransaction;
import com.clemble.casino.payment.service.PlayerAccountService;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.server.event.SystemEvent;
import com.clemble.casino.server.event.notification.SystemNotificationEvent;
import com.clemble.casino.server.event.payment.SystemPaymentFreezeRequestEvent;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.google.common.collect.ImmutableSet;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * Created by mavarazy on 1/17/15.
 */
public class GoalBetAspect extends GoalAspect<GoalChangedBetEvent> {

    final private PlayerAccountService accountService;
    final private SystemNotificationService notificationService;

    public GoalBetAspect(
        PlayerAccountService accountService,
        SystemNotificationService notificationService) {
        super(new EventTypeSelector(GoalChangedBetEvent.class));
        this.accountService = accountService;
        this.notificationService = notificationService;
    }

    @Override
    protected void doEvent(GoalChangedBetEvent event) {
        // Step 1. Fetching player bid
        PlayerBid playerBid = event.getBid();
        Money amount = playerBid.getBid().getAmount();
        // Step 2. Checking player can afford this bid
        boolean canAfford = accountService.canAfford(Collections.singleton(playerBid.getPlayer()), amount.getCurrency(), amount.getAmount()).isEmpty();
        if (!canAfford)
            throw new IllegalArgumentException();
        // Step 3. Sending freeze request
        Set<PaymentOperation> operations = ImmutableSet.<PaymentOperation>of(
            new PaymentOperation(playerBid.getPlayer(), amount, Operation.Credit),
            new PaymentOperation(PlayerAware.DEFAULT_PLAYER, amount, Operation.Debit)
        );
        SystemEvent freezeRequest = new SystemPaymentFreezeRequestEvent(new PendingTransaction(event.getBody().getGoalKey(), operations, null));
        notificationService.send(freezeRequest);
    }
}
