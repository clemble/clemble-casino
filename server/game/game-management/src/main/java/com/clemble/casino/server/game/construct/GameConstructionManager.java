package com.clemble.casino.server.game.construct;

import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.construct.GameRequest;

public interface GameConstructionManager<C extends GameRequest> {

    public GameConstruction register(C request);

}
