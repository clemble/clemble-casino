package com.gogomaya.server.game.action;

import com.gogomaya.server.game.action.move.GameMove;
import com.gogomaya.server.game.rule.GameRule;

public interface GameJudge<S extends GameState<?, ?>, M extends GameMove> extends GameEngine<S, M> {

    public GameRule getRule();

}
