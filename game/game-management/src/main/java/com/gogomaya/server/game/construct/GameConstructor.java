package com.gogomaya.server.game.construct;

import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.GameTable;
import com.gogomaya.server.game.construct.GameRequest;

public interface GameConstructor<State extends GameState, Request extends GameRequest> {

    public GameTable<State> construct(Request request);

}
