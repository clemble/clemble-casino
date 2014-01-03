package com.clemble.casino.integration.game;

import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.server.game.action.GameProcessorFactory;
import com.clemble.casino.server.game.action.impl.AbstractGameStateFactory;

public class NumberStateFactory extends AbstractGameStateFactory<NumberState> {

    public NumberStateFactory(GameProcessorFactory<NumberState> processorFactory) {
        super(processorFactory);
    }

    @Override
    public NumberState constructState(GameInitiation initiation, GameContext context) {
        return new NumberState(context, null, 0);
    }

}
