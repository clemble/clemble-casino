package com.gogomaya.server.game.judge;

import com.gogomaya.server.game.action.GameEngine;
import com.gogomaya.server.game.action.GameMove;
import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.rule.GameRule;

public interface GameJudge<S extends GameState<?, ?>, M extends GameMove> extends GameEngine<S, M> {

    public GameRule getRule();

}
