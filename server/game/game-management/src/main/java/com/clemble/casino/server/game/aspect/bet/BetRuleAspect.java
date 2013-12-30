package com.clemble.casino.server.game.aspect.bet;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.game.action.BetAction;
import com.clemble.casino.game.rule.bet.BetRule;
import com.clemble.casino.server.game.aspect.BasicGameAspect;

public class BetRuleAspect extends BasicGameAspect<BetAction> {

    final private BetRule betRule;

    public BetRuleAspect(BetRule betRule) {
        super(new EventTypeSelector(BetAction.class));
        this.betRule = betRule;
    }

    @Override
    public void doEvent(BetAction bet) {
        if (!betRule.isValid(bet)) {
            throw ClembleCasinoException.fromError(ClembleCasinoError.GamePlayBetInvalid);
        }
    }

}
