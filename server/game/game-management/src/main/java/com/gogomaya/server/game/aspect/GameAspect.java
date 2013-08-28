package com.gogomaya.server.game.aspect;

import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.GameSession;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.event.server.GameServerEvent;

public interface GameAspect<State extends GameState> {

    public void beforeMove(final State state, final ClientEvent move);

    public void afterMove(final State state, final GameServerEvent<State> events);

    public void afterGame(final GameSession<State> state, final GameServerEvent<State> events);

}
