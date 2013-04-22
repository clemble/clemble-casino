package com.gogomaya.server.game.action;

import java.io.Serializable;
import java.util.Collection;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import com.gogomaya.server.game.action.move.GameMove;
import com.gogomaya.server.game.tictactoe.action.TicTacToeState;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @Type(value = TicTacToeState.class, name = "ticTacToe") })
public interface GameState<M extends GameMove, S extends GamePlayerState> extends Serializable {

    public S getPlayerState(long playerId);

    public GameState<M, S> setPlayerState(S player);

    public M getNextMove(long playerId);

    public GameState<M, S> setNextMove(M move);

    public Collection<M> getNextMoves();

    public GameState<M, S> setNextMoves(Collection<M> nextMoves);

    public M getMadeMove(long playerId);

    public Collection<M> getMadeMoves();

    public GameState<M, S> setMadeMoves(Collection<M> moves);

    public GameState<M, S> addMadeMove(M move);

    public GameState<M, S> cleanMadeMove();

    public GamePlayerIterator getPlayerIterator();

    public GameState<M, S> setPlayerIterator(GamePlayerIterator playerIterator);

    public int getVersion();

    public GameState<M, S> incrementVersion();

}
