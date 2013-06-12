package com.gogomaya.server.game.build;

import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.GameTable;
import com.gogomaya.server.game.specification.GameSpecification;

public interface GameConstructionService<State extends GameState> {

    public GameTable<State> instantGame(final long playerId, final GameSpecification specification);

}
