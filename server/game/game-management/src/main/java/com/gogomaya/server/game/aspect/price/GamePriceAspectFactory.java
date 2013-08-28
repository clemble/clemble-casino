package com.gogomaya.server.game.aspect.price;

import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.aspect.GameAspect;
import com.gogomaya.server.game.aspect.GameAspectFactory;
import com.gogomaya.server.game.construct.GameInitiation;

public class GamePriceAspectFactory implements GameAspectFactory {

    @Override
    public <T extends GameState> GameAspect<T> construct(GameInitiation initiation) {
        return new GamePriceAspect<T>();
    }

}
