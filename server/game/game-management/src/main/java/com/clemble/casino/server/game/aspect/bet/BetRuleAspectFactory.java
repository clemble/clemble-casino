package com.clemble.casino.server.game.aspect.bet;

import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.action.BetAction;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.GameAspectFactory;

public class BetRuleAspectFactory implements GameAspectFactory<BetAction> {

    @Override
    public GameAspect<BetAction> construct(GameInitiation initiation, GameContext context) {
        return new BetRuleAspect(initiation.getSpecification().getBetRule());
    }

}
