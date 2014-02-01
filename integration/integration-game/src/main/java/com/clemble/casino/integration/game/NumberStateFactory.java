package com.clemble.casino.integration.game;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.server.game.action.GameStateFactory;

public class NumberStateFactory implements GameStateFactory<NumberState> {

    /**
     * Generated 01/02/14
     */
    private static final long serialVersionUID = -2792093350810646159L;

    @Override
    public Game getGame() {
        return Game.num;
    }

    @Override
    public NumberState constructState(GameInitiation initiation, GameContext context) {
        return new NumberState(context, null, 0);
    }

}
