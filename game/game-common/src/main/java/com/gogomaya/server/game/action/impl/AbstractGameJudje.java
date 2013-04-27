package com.gogomaya.server.game.action.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.server.game.action.GameEngine;
import com.gogomaya.server.game.action.GameJudge;
import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.move.GameMove;
import com.gogomaya.server.game.rule.GameRule;

abstract public class AbstractGameJudje<State extends GameState> implements GameJudge<State> {

    final GameEngine<State> gameEngine;
    final GameRule gameRule;

    protected AbstractGameJudje(final GameEngine<State> gameEngine, final GameRule gameRule) {
        this.gameEngine = checkNotNull(gameEngine);
        this.gameRule = checkNotNull(gameRule);
    }

    final protected State proceed(State oldState, GameMove gameMove) {
        return gameEngine.process(oldState, gameMove);
    }

    @Override
    final public GameRule getRule() {
        return gameRule;
    }

}
