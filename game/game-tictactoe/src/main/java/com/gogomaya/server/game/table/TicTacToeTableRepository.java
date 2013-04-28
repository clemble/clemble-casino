package com.gogomaya.server.game.table;

import com.gogomaya.server.game.action.GameTable;
import com.gogomaya.server.game.tictactoe.action.TicTacToeState;

public interface TicTacToeTableRepository extends GameTableRepository<GameTable<TicTacToeState>, TicTacToeState> {

}
