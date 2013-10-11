package com.clemble.casino.server.game.aspect;

import com.clemble.casino.event.ClientEvent;
import com.clemble.casino.game.GameSession;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.event.server.GameServerEvent;

public class BasicGameAspect<State extends GameState> implements GameAspect<State>{

    @Override
    public void beforeMove(State session, ClientEvent move) {
    }

    @Override
    public void afterMove(State state, GameServerEvent<State> events) {
    }

    @Override
    public void afterGame(GameSession<State> session, GameServerEvent<State> events) {
    }

}
