package com.clemble.casino.goal.aspect.bet;

import com.clemble.casino.bet.PlayerBet;
import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.goal.aspect.GoalAspect;
import com.clemble.casino.goal.lifecycle.management.event.GoalChangedBetEvent;
import com.clemble.casino.money.Money;
import com.clemble.casino.money.Operation;
import com.clemble.casino.payment.PaymentOperation;
import com.clemble.casino.payment.PendingTransaction;
import com.clemble.casino.payment.service.PlayerAccountService;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.server.event.SystemEvent;
import com.clemble.casino.server.event.payment.SystemPaymentFreezeRequestEvent;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.google.common.collect.ImmutableSet;

import java.util.Collections;
import java.util.Set;

/**
 * Created by mavarazy on 1/17/15.
 */
public class GoalBetPaymentAspect extends GoalAspect<GoalChangedBetEvent> {

    final private PlayerAccountService accountService;
    final private SystemNotificationService notificationService;

    public GoalBetPaymentAspect(
        PlayerAccountService accountService,
        SystemNotificationService notificationService) {
        super(new EventTypeSelector(GoalChangedBetEvent.class));
        this.accountService = accountService;
        this.notificationService = notificationService;
    }

    @Override
    protected void doEvent(GoalChangedBetEvent event) {
        // Step 1. Fetching player bid
        PlayerBet playerBid = event.getBet();
        Money amount = playerBid.getBet().getAmount();
        // Step 2. Checking player can afford this bid
        boolean canAfford = accountService.canAfford(Collections.singleton(playerBid.getPlayer()), amount.getCurrency(), amount.getAmount()).isEmpty();
        if (!canAfford)
            throw ClembleCasinoException.fromError(ClembleCasinoError.AccountInsufficientAmount);
        // Step 3. Sending freeze request
        SystemEvent freezeRequest = SystemPaymentFreezeRequestEvent.create(event.getBody().getGoalKey(), playerBid.getPlayer(), amount);
        notificationService.send(freezeRequest);
    }
}
