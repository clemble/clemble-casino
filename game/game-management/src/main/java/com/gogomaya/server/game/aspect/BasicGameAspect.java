package com.gogomaya.server.game.aspect;

import java.util.Collection;

import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.GameSession;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.event.server.GameServerEvent;

public class BasicGameAspect<State extends GameState> implements GameAspect<State>{

    @Override
    public void beforeMove(GameSession<State> session, ClientEvent move) {
    }

    @Override
    public Collection<GameServerEvent<State>> afterMove(GameSession<State> session, Collection<GameServerEvent<State>> events) {
        return events;
    }

    @Override
    public Collection<GameServerEvent<State>> afterGame(GameSession<State> session, Collection<GameServerEvent<State>> events) {
        return events;
    }

}
