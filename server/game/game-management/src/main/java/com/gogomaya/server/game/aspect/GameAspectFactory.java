package com.gogomaya.server.game.aspect;

import com.gogomaya.game.GameState;
import com.gogomaya.game.construct.GameInitiation;

public interface GameAspectFactory {

    public <T extends GameState> GameAspect<T> construct(GameInitiation construction);

}
