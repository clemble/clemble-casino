package com.gogomaya.server.game.build;

import java.util.Collection;

import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.GameTable;
import com.gogomaya.server.game.specification.GameSpecification;

public interface GameConstructionService<State extends GameState> {

    public GameTable<State> instantGame(final long playerId, final GameSpecification specification);

    public GameTable<State> avilabilityGame(final long playerId, final Collection<Long> opponenents, final GameSpecification specification);

}
