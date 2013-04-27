package com.gogomaya.server.game.session;

import org.springframework.stereotype.Repository;

import com.gogomaya.server.game.tictactoe.TicTacToeSession;

@Repository
public interface TicTacToeSessionRepository extends GameSessionRepository<TicTacToeSession> {

}
