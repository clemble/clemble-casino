package com.gogomaya.server.game.aspect;

import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.GameSession;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.event.server.GameServerEvent;

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
