package com.gogomaya.server.game.action;

import com.gogomaya.event.ClientEvent;
import com.gogomaya.game.GameSession;
import com.gogomaya.game.GameState;
import com.gogomaya.game.event.server.GameServerEvent;

public interface GameProcessor<State extends GameState> {

    public GameServerEvent<State> process(final GameSession<State> session, final ClientEvent move);

}
