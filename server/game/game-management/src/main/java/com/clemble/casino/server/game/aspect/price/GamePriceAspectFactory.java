package com.clemble.casino.server.game.aspect.price;

import org.springframework.core.Ordered;

import com.clemble.casino.game.action.BetAction;
import com.clemble.casino.game.construct.ServerGameInitiation;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.GameAspectFactory;

public class GamePriceAspectFactory implements GameAspectFactory<BetAction> {

    @Override
    public GameAspect<BetAction> construct(ServerGameInitiation initiation) {
        return new GamePriceAspect(initiation.getContext());
    }

    @Override
    public int getOrder(){
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
