package com.clemble.casino.server.game.aspect.bet;

import org.springframework.core.Ordered;

import com.clemble.casino.game.MatchGameContext;
import com.clemble.casino.game.action.BetAction;
import com.clemble.casino.game.specification.MatchGameConfiguration;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.MatchGameAspectFactory;

public class BetRuleAspectFactory implements MatchGameAspectFactory<BetAction> {

    @Override
    public GameAspect<BetAction> construct(MatchGameConfiguration configuration, MatchGameContext context) {
        return new BetRuleAspect(configuration.getBetRule());
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
