package com.gogomaya.server.game.action;

import java.io.Serializable;
import java.util.Collection;

public interface GameState<M extends GameMove, S extends GamePlayerState> extends Serializable {

    public Collection<S> getPlayerStates();

    public Collection<M> getNextMoves();

    public Collection<M> getMadeMoves();

    public S getPlayerState(long playerId);

    public M getNextMove(long playerId);

    public M getMadeMove(long playerId);

}
