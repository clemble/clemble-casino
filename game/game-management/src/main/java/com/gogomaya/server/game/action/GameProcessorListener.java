package com.gogomaya.server.game.action;

import java.util.Collection;

import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.GameSession;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.event.server.GameEvent;

public interface GameProcessorListener<State extends GameState> {

    abstract public void beforeMove(final GameSession<State> session, final ClientEvent move);

    abstract public Collection<GameEvent<State>> afterMove(final GameSession<State> session, final Collection<GameEvent<State>> events);

}
