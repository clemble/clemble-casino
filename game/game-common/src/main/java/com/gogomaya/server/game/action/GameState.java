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
public interface GameState extends Serializable {

    public GamePlayerState getPlayerState(long playerId);

    public GameState setPlayerState(GamePlayerState player);

    public GameState setPlayerStates(Collection<GamePlayerState> playersStates);

    public GameMove getNextMove(long playerId);

    public GameState setNextMove(GameMove move);

    public Collection<GameMove> getNextMoves();

    public GameState setNextMoves(Collection<GameMove> nextMoves);

    public GameMove getMadeMove(long playerId);

    public Collection<GameMove> getMadeMoves();

    public GameState setMadeMoves(Collection<GameMove> moves);

    public GameState addMadeMove(GameMove move);

    public GameState cleanMadeMove();

    public GamePlayerIterator getPlayerIterator();

    public GameState setPlayerIterator(GamePlayerIterator playerIterator);

    public int getVersion();

    public GameState incrementVersion();

    public boolean complete();

}
