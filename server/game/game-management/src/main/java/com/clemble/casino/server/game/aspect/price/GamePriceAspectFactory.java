package com.clemble.casino.server.game.aspect.price;

import org.springframework.core.Ordered;

import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.action.BetAction;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.GameAspectFactory;

public class GamePriceAspectFactory implements GameAspectFactory<BetAction> {

    @Override
    public GameAspect<BetAction> construct(GameInitiation initiation, GameContext context) {
        return new GamePriceAspect(context);
    }

    @Override
    public int getOrder(){
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
