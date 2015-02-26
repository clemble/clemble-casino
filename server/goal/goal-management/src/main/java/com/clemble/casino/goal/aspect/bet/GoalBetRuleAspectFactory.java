package com.clemble.casino.goal.aspect.bet;

import com.clemble.casino.goal.aspect.GenericGoalAspectFactory;
import com.clemble.casino.goal.lifecycle.configuration.GoalConfiguration;
import com.clemble.casino.goal.lifecycle.management.GoalState;
import com.clemble.casino.goal.lifecycle.management.event.GoalChangedBetEvent;
import com.clemble.casino.lifecycle.management.event.action.PlayerAction;
import com.clemble.casino.lifecycle.management.event.action.bet.BetAction;
import com.clemble.casino.server.aspect.ClembleAspect;
import org.springframework.core.Ordered;

/**
 * Created by mavarazy on 2/26/15.
 */
public class GoalBetRuleAspectFactory implements GenericGoalAspectFactory<PlayerAction<BetAction>> {

    @Override
    public ClembleAspect<PlayerAction<BetAction>> construct(GoalConfiguration configuration, GoalState state) {
        return new GoalBetRuleAspect(configuration.getSupporterConfiguration().getBetRule());
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE - 11;
    }
}
