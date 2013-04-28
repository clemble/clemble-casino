package com.gogomaya.server.integration.tictactoe;

import java.util.List;

import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.tictactoe.action.move.TicTacToeMove;

public interface TicTacToeOperations {

    public List<TicTacToePlayer> start();

    public TicTacToePlayer start(GameSpecification specification);

    public void select(TicTacToePlayer player, int row, int column);

    public void bet(TicTacToePlayer player, int ammount);

    public void perform(TicTacToePlayer player, TicTacToeMove action);

}
