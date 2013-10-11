package com.clemble.casino.server.game.aspect;

import com.clemble.casino.game.GameState;
import com.clemble.casino.game.construct.GameInitiation;

public interface GameAspectFactory {

    public <T extends GameState> GameAspect<T> construct(GameInitiation construction);

}
