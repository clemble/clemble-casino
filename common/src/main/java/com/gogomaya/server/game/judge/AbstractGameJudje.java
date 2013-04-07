package com.gogomaya.server.game.judge;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.server.game.action.GameEngine;
import com.gogomaya.server.game.action.GameMove;
import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.rule.GameRule;

abstract public class AbstractGameJudje<S extends GameState<?, ?>, M extends GameMove> implements GameJudge<S, M> {

    final GameEngine<S, M> gameEngine;
    final GameRule gameRule;

    protected AbstractGameJudje(final GameEngine<S, M> gameEngine, final GameRule gameRule) {
        this.gameEngine = checkNotNull(gameEngine);
        this.gameRule = checkNotNull(gameRule);
    }

    final protected S proceed(S oldState, M gameMove) {
        return gameEngine.process(oldState, gameMove);
    }

    @Override
    final public GameRule getRule() {
        return gameRule;
    }

}
