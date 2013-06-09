package com.gogomaya.server.game.action;

import java.util.List;

import com.gogomaya.server.game.specification.GameSpecification;

public interface GameStateFactory<State extends GameState> {

    public State create();

    public void restore(final GameSession<State> gameSession);

    public State create(final GameSpecification gameSpecification, final List<Long> playerIds);

}
