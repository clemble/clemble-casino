package com.gogomaya.server.game.outcome;

import com.gogomaya.server.game.action.GameSession;
import com.gogomaya.server.game.action.GameState;

public interface GameOutcomeService<State extends GameState> {

    public void finished(GameSession<State> gameTable);

}
