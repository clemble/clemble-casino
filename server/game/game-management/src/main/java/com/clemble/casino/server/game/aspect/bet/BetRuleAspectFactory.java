package com.clemble.casino.server.game.aspect.bet;

import com.clemble.casino.game.RoundGameContext;
import com.clemble.casino.game.configuration.RoundGameConfiguration;
import org.springframework.core.Ordered;

import com.clemble.casino.game.action.BetAction;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.RoundGameAspectFactory;

public class BetRuleAspectFactory implements RoundGameAspectFactory<BetAction> {

    @Override
    public GameAspect<BetAction> construct(RoundGameConfiguration configuration, RoundGameContext context) {
        return new BetRuleAspect(context, configuration.getBetRule());
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
