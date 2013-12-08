package com.clemble.casino.server.game.aspect;

import java.util.Collection;

import com.clemble.casino.event.Event;
import com.clemble.casino.game.GameSession;
import com.clemble.casino.game.GameState;

abstract public class BasicGameManagementAspect implements GameManagementAspect {

    @Override
    public <State extends GameState> void afterGame(GameSession<State> state) {
    }

    @Override
    public <State extends GameState> void changed(State state, Collection<Event> events) {
    }

}
