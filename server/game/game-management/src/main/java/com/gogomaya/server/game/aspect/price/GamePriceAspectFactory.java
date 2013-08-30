package com.gogomaya.server.game.aspect.price;

import com.gogomaya.game.GameState;
import com.gogomaya.game.construct.GameInitiation;
import com.gogomaya.server.game.aspect.GameAspect;
import com.gogomaya.server.game.aspect.GameAspectFactory;

public class GamePriceAspectFactory implements GameAspectFactory {

    @Override
    public <T extends GameState> GameAspect<T> construct(GameInitiation initiation) {
        return new GamePriceAspect<T>();
    }

}
