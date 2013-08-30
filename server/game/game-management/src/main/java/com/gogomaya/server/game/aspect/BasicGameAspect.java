package com.gogomaya.server.game.aspect;

import com.gogomaya.event.ClientEvent;
import com.gogomaya.game.GameSession;
import com.gogomaya.game.GameState;
import com.gogomaya.game.event.server.GameServerEvent;

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
