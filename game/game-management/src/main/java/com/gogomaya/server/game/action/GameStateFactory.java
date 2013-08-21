package com.gogomaya.server.game.action;

import com.gogomaya.server.game.GameSession;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.construct.GameInitiation;

public interface GameStateFactory<State extends GameState> {

    public State constructState(final GameInitiation initiation);

    public State constructState(final GameSession<State> gameSession);

}
