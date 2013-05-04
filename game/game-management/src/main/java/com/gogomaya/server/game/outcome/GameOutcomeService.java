package com.gogomaya.server.game.outcome;

import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.GameTable;

public interface GameOutcomeService<State extends GameState> {

    public void finished(GameTable<State> gameTable);

}
