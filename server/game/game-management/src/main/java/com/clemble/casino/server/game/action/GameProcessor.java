package com.clemble.casino.server.game.action;

import com.clemble.casino.game.GameSession;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.event.client.GameAction;
import com.clemble.casino.game.event.server.GameManagementEvent;

public interface GameProcessor<State extends GameState> {

    public GameManagementEvent<State> process(final GameSession<State> session, final GameAction move);

}
