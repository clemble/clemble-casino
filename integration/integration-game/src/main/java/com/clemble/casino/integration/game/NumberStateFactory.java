package com.clemble.casino.integration.game;

import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.server.game.action.GameStateFactory;

public class NumberStateFactory implements GameStateFactory<NumberState> {

    @Override
    public NumberState constructState(GameInitiation initiation, GameContext context) {
        return new NumberState(context, null, 0);
    }

}
