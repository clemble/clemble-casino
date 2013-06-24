package com.gogomaya.server.game.construct;

import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.GameTable;

public interface GameConstruction<State extends GameState, Request extends GameRequest> {

    public GameTable<State> construct(Request request);

}
