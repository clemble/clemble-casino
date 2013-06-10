package com.gogomaya.server.game.action;

import java.util.Collection;

import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.GameSession;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.event.server.GameEvent;

public interface GameProcessor<State extends GameState> {

    public Collection<GameEvent<State>> process(final GameSession<State> session, final ClientEvent move);

}
