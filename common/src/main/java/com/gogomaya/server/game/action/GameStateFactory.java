package com.gogomaya.server.game.action;

import java.util.Set;

import com.gogomaya.server.game.GameSpecification;

public interface GameStateFactory {

    public GameState<?, ?> initialize(final GameSpecification gameSpecification, final Set<Long> playerIds);

}
