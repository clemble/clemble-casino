package com.gogomaya.server.game.table;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gogomaya.server.game.tictactoe.action.TicTacToeTable;

public interface TicTacToeTableRepository extends JpaRepository<TicTacToeTable, Long> {

    TicTacToeTable findByPlayersIn(String player);

}
