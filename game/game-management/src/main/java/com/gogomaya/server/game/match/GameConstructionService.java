package com.gogomaya.server.game.match;

import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.GameTable;
import com.gogomaya.server.game.specification.GameSpecification;

public interface GameConstructionService<State extends GameState> {

    public GameTable<State> findOpponent(final long playerId, final GameSpecification specification);

}
