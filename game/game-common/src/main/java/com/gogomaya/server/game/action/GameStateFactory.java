package com.gogomaya.server.game.action;

import java.util.Set;

import com.gogomaya.server.game.specification.GameSpecification;

public interface GameStateFactory<State extends GameState> {

    public State create(final GameSession gameSession);

    public State create(final GameSpecification gameSpecification, final Set<Long> playerIds);

}
