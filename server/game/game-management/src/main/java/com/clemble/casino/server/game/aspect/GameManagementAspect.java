package com.clemble.casino.server.game.aspect;

import com.clemble.casino.game.GameSession;
import com.clemble.casino.game.GameState;

public interface GameManagementAspect {

    public <State extends GameState> void afterGame(final GameSession<State> state);

}
