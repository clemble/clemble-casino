package com.gogomaya.server.integration.tictactoe;

import org.jbehave.core.annotations.When;

import com.gogomaya.server.game.tictactoe.action.TicTacToeState;
import com.gogomaya.server.game.tictactoe.action.move.TicTacToeMove;
import com.gogomaya.server.integration.game.GamePlayerFactory;

public interface TicTacToeOperations extends GamePlayerFactory<TicTacToeState>{

    @When("$A select $row $ column")
    public TicTacToeState select(TicTacToePlayer player, int row, int column);

    @When("$A bets $bet")
    public TicTacToeState bet(TicTacToePlayer player, int ammount);

    @When("$A preforms $move")
    public TicTacToeState perform(TicTacToePlayer player, TicTacToeMove action);

}
