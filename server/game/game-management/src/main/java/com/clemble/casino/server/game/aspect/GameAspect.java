package com.clemble.casino.server.game.aspect;

import com.clemble.casino.game.GameSession;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.event.client.GameAction;
import com.clemble.casino.game.event.server.GameManagementEvent;

public interface GameAspect<State extends GameState> {

    public void beforeMove(final State state, final GameAction move);

    public void afterMove(final State state, final GameManagementEvent<State> events);

    public void afterGame(final GameSession<State> state, final GameManagementEvent<State> events);

}
