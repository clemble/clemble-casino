package com.gogomaya.server.game.aspect;

import com.gogomaya.event.ClientEvent;
import com.gogomaya.game.GameSession;
import com.gogomaya.game.GameState;
import com.gogomaya.game.event.server.GameServerEvent;

public interface GameAspect<State extends GameState> {

    public void beforeMove(final State state, final ClientEvent move);

    public void afterMove(final State state, final GameServerEvent<State> events);

    public void afterGame(final GameSession<State> state, final GameServerEvent<State> events);

}
