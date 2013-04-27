package com.gogomaya.server.game.action;

import com.gogomaya.server.game.rule.GameRule;

public interface GameJudge<State extends GameState> extends GameEngine<State> {

    public GameRule getRule();

}
