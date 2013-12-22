package com.clemble.casino.server.game.aspect;

import com.clemble.casino.game.GameSession;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.action.GameAction;
import com.clemble.casino.game.event.server.GameManagementEvent;

public class BasicGameAspect<State extends GameState> implements GameAspect<State>{

    @Override
    public void beforeMove(State session, GameAction move) {
    }

    @Override
    public void afterMove(State state, GameManagementEvent<State> events) {
    }

    @Override
    public void afterGame(GameSession<State> session, GameManagementEvent<State> events) {
    }

}
