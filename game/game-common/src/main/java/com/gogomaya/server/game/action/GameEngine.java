package com.gogomaya.server.game.action;

import com.gogomaya.server.game.action.move.GameMove;

public interface GameEngine<State extends GameState> {

    public State process(final State oldState, final GameMove gameMove);

}
