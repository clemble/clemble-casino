package com.clemble.casino.server.game.aspect;

import com.clemble.casino.game.construct.GameInitiation;

public interface GameManagementAspecteFactory {

    public GameManagementAspect construct(GameInitiation initiation);

}
