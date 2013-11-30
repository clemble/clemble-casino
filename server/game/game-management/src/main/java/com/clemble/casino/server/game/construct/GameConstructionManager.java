package com.clemble.casino.server.game.construct;

import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.construct.PlayerGameConstructionRequest;

public interface GameConstructionManager<C extends PlayerGameConstructionRequest> {

    public GameConstruction register(C request, String id);

}
