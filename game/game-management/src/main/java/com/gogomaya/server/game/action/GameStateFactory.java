package com.gogomaya.server.game.action;

import java.util.Collection;

import com.gogomaya.server.game.GameSession;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.construct.GameInitiation;
import com.gogomaya.server.game.specification.GameSpecification;

public interface GameStateFactory<State extends GameState> {

    public State emptyState();

    public State constructState(final GameInitiation initiation);

    public State constructState(final GameSession<State> gameSession);

    public State constructState(final GameSpecification gameSpecification, final Collection<Long> playerIds);

}
