package com.gogomaya.server.game.action;


public interface GameProcessor<S extends GameState<?, ?>, M extends GameMove> {

    public S process(final S oldState, final M gameMove);

}
