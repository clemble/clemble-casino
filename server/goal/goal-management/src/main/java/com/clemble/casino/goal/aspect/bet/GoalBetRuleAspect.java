package com.clemble.casino.goal.aspect.bet;

import com.clemble.casino.client.PlayerActionTypeSelector;
import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.goal.aspect.GoalAspect;
import com.clemble.casino.lifecycle.configuration.rule.bet.BetRule;
import com.clemble.casino.lifecycle.management.event.action.PlayerAction;
import com.clemble.casino.lifecycle.management.event.action.bet.BetAction;

/**
 * Created by mavarazy on 2/26/15.
 */
public class GoalBetRuleAspect extends GoalAspect<PlayerAction<BetAction>> {

    final private BetRule betRule;

    public GoalBetRuleAspect(BetRule betRule){
        super(new PlayerActionTypeSelector(BetAction.class));
        this.betRule = betRule;
    }

    @Override
    protected void doEvent(PlayerAction<BetAction> event) {
        if (!betRule.isValid(event.getAction()))
            throw ClembleCasinoException.fromError(ClembleCasinoError.GoalBidInvalid);
    }
}
