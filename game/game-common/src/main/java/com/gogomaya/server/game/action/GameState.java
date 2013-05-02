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

    public Collection<GameMove> getNextMoves();

    public GameMove getMadeMove(long playerId);

    public Collection<GameMove> getMadeMoves();

    public GamePlayerIterator getPlayerIterator();

    public GameState setPlayerIterator(GamePlayerIterator playerIterator);

    public boolean complete();

    public int getVersion();

}
