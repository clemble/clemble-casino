package com.clemble.casino.integration.game;

import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.server.game.action.GameProcessorFactory;
import com.clemble.casino.server.game.action.impl.AbstractGameStateFactory;
import com.clemble.casino.server.repository.game.GameConstructionRepository;

public class NumberStateFactory extends AbstractGameStateFactory<NumberState> {

    public NumberStateFactory(GameConstructionRepository constructionRepository, GameProcessorFactory<NumberState> processorFactory) {
        super(constructionRepository, processorFactory);
    }

    @Override
    public NumberState constructState(GameInitiation initiation, GameContext context) {
        return new NumberState(context, null, null, 0);
    }

}
