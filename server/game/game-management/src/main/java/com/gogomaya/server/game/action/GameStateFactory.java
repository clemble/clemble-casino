package com.gogomaya.server.game.action;

import com.gogomaya.game.GameSession;
import com.gogomaya.game.GameState;
import com.gogomaya.game.construct.GameInitiation;

public interface GameStateFactory<State extends GameState> {

    public State constructState(final GameInitiation initiation);

    public State constructState(final GameSession<State> gameSession);

}
