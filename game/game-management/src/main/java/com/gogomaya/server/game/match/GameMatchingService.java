package com.gogomaya.server.game.match;

import com.gogomaya.server.game.GameSpecification;
import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.GameTable;

public interface GameMatchingService<State extends GameState, Spec extends GameSpecification> {

    public GameTable<State> reserve(final long playerId, final Spec gameSpecification);

}
