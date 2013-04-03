package com.gogomaya.server.game.action;

import java.io.Serializable;
import java.util.Collection;

public interface GameState<M extends GameMove, S extends GamePlayerState> extends Serializable {

    public S getPlayerState(long playerId);

    public void setPlayerState(S player);

    public M getNextMove(long playerId);

    public void setNextMove(M move);

    public void setNextMoves(Collection<M> nextMoves);

    public M getMadeMove(long playerId);

    public void addMadeMove(M move);

    public void cleanMadeMove();

    public GamePlayerIterator getPlayerIterator();
}
