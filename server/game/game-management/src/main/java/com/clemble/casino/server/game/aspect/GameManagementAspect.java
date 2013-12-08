package com.clemble.casino.server.game.aspect;

import java.util.Collection;

import com.clemble.casino.event.Event;
import com.clemble.casino.game.GameSession;
import com.clemble.casino.game.GameState;

public interface GameManagementAspect {

    public <State extends GameState> void changed(final State state,final Collection<Event> events);

    public <State extends GameState> void afterGame(final GameSession<State> state);

}
