package com.clemble.casino.server.game.action;

import com.clemble.casino.event.ClientEvent;
import com.clemble.casino.game.GameSession;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.event.server.GameServerEvent;

public interface GameProcessor<State extends GameState> {

    public GameServerEvent<State> process(final GameSession<State> session, final ClientEvent move);

}
