package com.gogomaya.server.game.construct;

public interface GameConstructionManager<C extends GameRequest> {

    public GameConstruction register(C request);

}
