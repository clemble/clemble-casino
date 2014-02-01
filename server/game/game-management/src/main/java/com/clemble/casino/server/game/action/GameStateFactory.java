package com.clemble.casino.server.game.action;

import com.clemble.casino.game.GameAware;
import com.clemble.casino.game.MatchGameContext;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.construct.GameInitiation;

public interface GameStateFactory<State extends GameState> extends GameAware {

    public State constructState(final GameInitiation initiation, final MatchGameContext context);

}
