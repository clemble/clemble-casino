package com.gogomaya.server.game.action;

import java.util.Collection;

import com.gogomaya.server.game.action.move.GameMove;
import com.gogomaya.server.game.event.GameEvent;

public interface GameProcessor<State extends GameState> {

    public Collection<GameEvent<State>> process(final GameSession<State> session, final State state, final GameMove move);

}
