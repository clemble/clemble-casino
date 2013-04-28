package com.gogomaya.server.game.action;

import java.util.Set;

import com.gogomaya.server.game.specification.GameSpecification;

public interface GameStateFactory<State extends GameState> {

    public State initialize(final GameSpecification gameSpecification, final Set<Long> playerIds);

}
