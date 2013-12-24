package com.clemble.casino.server.game.aspect.price;

import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.GameAspectFactory;

public class GamePriceAspectFactory implements GameAspectFactory {

    @Override
    public GameAspect construct(GameInitiation initiation, GameContext context) {
        return new GamePriceAspect(context.getAccount());
    }

}
