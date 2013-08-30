package com.gogomaya.server.game.construct;

import com.gogomaya.game.construct.GameConstruction;
import com.gogomaya.game.construct.GameRequest;

public interface GameConstructionManager<C extends GameRequest> {

    public GameConstruction register(C request);

}
