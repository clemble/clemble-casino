package com.gogomaya.server.game.session;

import org.springframework.stereotype.Repository;

import com.gogomaya.server.game.tictactoe.TicTacToeSession;
import com.gogomaya.server.game.tictactoe.action.TicTacToeState;

@Repository
public interface TicTacToeSessionRepository extends GameSessionRepository<TicTacToeSession, TicTacToeState> {

}
