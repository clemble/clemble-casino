package com.gogomaya.server.game.action;

import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.GameSession;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.event.server.GameServerEvent;

public interface GameProcessor<State extends GameState> {

    public GameServerEvent<State> process(final GameSession<State> session, final ClientEvent move);

}
