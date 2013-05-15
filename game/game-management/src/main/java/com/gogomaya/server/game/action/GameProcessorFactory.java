package com.gogomaya.server.game.action;

import com.gogomaya.server.game.action.impl.VerificationGameProcessor;
import com.gogomaya.server.game.specification.GameSpecification;

public class GameProcessorFactory<State extends GameState> {

    final private GameProcessor<State> coreProcessor;

    public GameProcessorFactory(final GameProcessor<State> coreProcessor) {
        this.coreProcessor = coreProcessor;
    }

    public GameProcessor<State> create(GameSpecification specification) {
        return new VerificationGameProcessor<State>(coreProcessor);
    }

}
