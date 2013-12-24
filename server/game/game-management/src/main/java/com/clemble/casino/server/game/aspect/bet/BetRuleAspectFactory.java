package com.clemble.casino.server.game.aspect.bet;

import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.GameAspectFactory;

public class BetRuleAspectFactory implements GameAspectFactory {

    @Override
    public GameAspect construct(GameInitiation initiation, GameContext context) {
        return new BetRuleAspect(initiation.getSpecification().getBetRule());
    }

}
