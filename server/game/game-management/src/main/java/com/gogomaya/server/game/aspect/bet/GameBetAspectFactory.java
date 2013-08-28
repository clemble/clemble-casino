package com.gogomaya.server.game.aspect.bet;

import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.aspect.GameAspect;
import com.gogomaya.server.game.aspect.GameAspectFactory;
import com.gogomaya.server.game.construct.GameInitiation;

public class GameBetAspectFactory implements GameAspectFactory {

    @Override
    public <T extends GameState> GameAspect<T> construct(GameInitiation initiation) {
        return new GameBetAspect<>(initiation.getSpecification().getBetRule());
    }

}
