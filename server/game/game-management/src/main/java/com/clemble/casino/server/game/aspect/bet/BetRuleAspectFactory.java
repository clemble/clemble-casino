package com.clemble.casino.server.game.aspect.bet;

import com.clemble.casino.game.RoundGameContext;
import com.clemble.casino.event.bet.BetEvent;
import com.clemble.casino.game.configuration.RoundGameConfiguration;
import com.clemble.casino.server.game.aspect.bet.BetRuleAspect;

import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.RoundGameAspectFactory;

public class BetRuleAspectFactory implements RoundGameAspectFactory<BetEvent> {

    @Override
    public GameAspect<BetEvent> construct(RoundGameConfiguration configuration, RoundGameContext context) {
        return new BetRuleAspect(context, configuration.getBetRule());
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }

}
