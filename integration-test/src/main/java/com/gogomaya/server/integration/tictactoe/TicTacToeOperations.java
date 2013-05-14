package com.gogomaya.server.integration.tictactoe;

import java.util.List;

import org.jbehave.core.annotations.When;

import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.tictactoe.action.TicTacToeState;
import com.gogomaya.server.game.tictactoe.action.move.TicTacToeMove;

public interface TicTacToeOperations {

    public List<TicTacToePlayer> start();

    @When("$A plays $specification")
    public TicTacToePlayer start(GameSpecification specification);

    @When("$A select $row $ column")
    public TicTacToeState select(TicTacToePlayer player, int row, int column);

    @When("$A bets $bet")
    public TicTacToeState bet(TicTacToePlayer player, int ammount);

    @When("$A preforms $move")
    public TicTacToeState perform(TicTacToePlayer player, TicTacToeMove action);

}
