package com.gogomaya.server.game.aspect;

import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.action.GameProcessor;
import com.gogomaya.server.game.specification.GameSpecification;

public interface GameAspectFactory {

    public <T extends GameState> GameProcessor<T> construct(GameSpecification gameSpecification);

}
