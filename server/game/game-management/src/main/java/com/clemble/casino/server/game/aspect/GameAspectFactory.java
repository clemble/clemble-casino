package com.clemble.casino.server.game.aspect;

import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.construct.GameInitiation;

public interface GameAspectFactory {

    public GameAspect construct(GameInitiation initiation, GameContext construction);

}
