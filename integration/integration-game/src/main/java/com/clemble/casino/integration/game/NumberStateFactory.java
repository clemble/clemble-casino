package com.clemble.casino.integration.game;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.lifecycle.management.RoundGameContext;
import com.clemble.casino.game.lifecycle.initiation.GameInitiation;
import com.clemble.casino.game.lifecycle.management.RoundStateFactory;

public class NumberStateFactory implements RoundStateFactory<NumberState> {

    /**
     * Generated 01/02/14
     */
    private static final long serialVersionUID = -2792093350810646159L;

    @Override
    public Game getGame() {
        return Game.num;
    }

    @Override
    public NumberState constructState(GameInitiation initiation, RoundGameContext context) {
        // Step 1. Setting next
        context.getActionLatch().expectNext(context.getSessionKey(), context.getPlayerIterator().getPlayers(), SelectNumberAction.class);
        return new NumberState();
    }

}
