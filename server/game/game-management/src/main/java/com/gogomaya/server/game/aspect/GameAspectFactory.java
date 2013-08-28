package com.gogomaya.server.game.aspect;

import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.construct.GameInitiation;

public interface GameAspectFactory {

    public <T extends GameState> GameAspect<T> construct(GameInitiation construction);

}
