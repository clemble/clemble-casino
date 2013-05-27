package com.gogomaya.server.game.action;

import java.io.Serializable;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gogomaya.server.game.action.move.GameMove;
import com.gogomaya.server.game.tictactoe.action.TicTacToeState;
// !!! NEED TO MAKE A COPY OF THIS ON EVERY OBJECT WITH GET METHOD CALL
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

    public GameOutcome getOutcome();

    public boolean complete();

    public int getVersion();

    public int increaseVersion();

}
