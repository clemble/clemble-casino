package com.clemble.casino.goal.aspect.bet;

import com.clemble.casino.goal.aspect.GenericGoalAspectFactory;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.lifecycle.management.event.GoalChangedBetEvent;
import com.clemble.casino.payment.service.PlayerAccountService;
import com.clemble.casino.server.aspect.ClembleAspect;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import org.springframework.core.Ordered;

/**
 * Created by mavarazy on 1/17/15.
 */
public class GoalBetAspectFactory implements GenericGoalAspectFactory<GoalChangedBetEvent> {

    final private PlayerAccountService accountService;
    final private SystemNotificationService notificationService;

    final private GoalBetAspect INSTANCE;

    public GoalBetAspectFactory(PlayerAccountService accountService, SystemNotificationService notificationService) {
        this.accountService = accountService;
        this.notificationService = notificationService;

        this.INSTANCE = new GoalBetAspect(accountService, notificationService);
    }

    @Override
    public ClembleAspect<GoalChangedBetEvent> construct(GoalConfiguration configuration, GoalState state) {
        return INSTANCE;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
