package com.clemble.casino.server.game.aspect;

import com.clemble.casino.event.ClientEvent;
import com.clemble.casino.game.GameSession;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.event.server.GameServerEvent;

public interface GameAspect<State extends GameState> {

    public void beforeMove(final State state, final ClientEvent move);

    public void afterMove(final State state, final GameServerEvent<State> events);

    public void afterGame(final GameSession<State> state, final GameServerEvent<State> events);

}
