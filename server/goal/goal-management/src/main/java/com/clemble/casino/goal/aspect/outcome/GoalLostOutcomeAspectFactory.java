package com.clemble.casino.goal.aspect.outcome;

import com.clemble.casino.bet.PlayerBid;
import com.clemble.casino.goal.aspect.GoalAspectFactory;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.lifecycle.management.event.GoalEndedEvent;
import com.clemble.casino.server.aspect.ClembleAspect;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import org.springframework.core.Ordered;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by mavarazy on 10/9/14.
 */
public class GoalLostOutcomeAspectFactory implements GoalAspectFactory<GoalEndedEvent>{

    final private SystemNotificationService systemNotificationService;

    public GoalLostOutcomeAspectFactory(SystemNotificationService systemNotificationService) {
        this.systemNotificationService = systemNotificationService;
    }

    @Override
    public ClembleAspect<GoalEndedEvent> construct(GoalConfiguration configuration, GoalState context) {
                // Step 1. Generating player bids
        Collection<PlayerBid> playerBids = new ArrayList<PlayerBid>();
        // Step 2. Checking player bids
        Collection<PlayerBid> bids = new ArrayList<PlayerBid>(context.getBids());
        bids.add(new PlayerBid(context.getPlayer(), context.getConfiguration().getBid()));
        // Step 3. Checking player bid
        return new GoalLostOutcomeAspect(bids, systemNotificationService);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 2;
    }

}
