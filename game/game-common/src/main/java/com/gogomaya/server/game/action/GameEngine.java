package com.gogomaya.server.game.action;

import com.gogomaya.server.game.action.move.GameMove;


public interface GameEngine<S extends GameState<?, ?>, M extends GameMove> {

    public S process(final S oldState, final M gameMove);

}
