package com.gogomaya.server.integration.emulation.tictactoe;

import java.util.Random;

import com.gogomaya.server.game.action.move.GameMove;
import com.gogomaya.server.game.tictactoe.action.TicTacToeCellState;
import com.gogomaya.server.game.tictactoe.action.TicTacToeState;
import com.gogomaya.server.game.tictactoe.action.move.TicTacToeBetOnCellMove;
import com.gogomaya.server.game.tictactoe.action.move.TicTacToeSelectCellMove;
import com.gogomaya.server.integration.emulator.GameActor;
import com.gogomaya.server.integration.game.GamePlayer;
import com.gogomaya.server.integration.tictactoe.TicTacToePlayer;

public class TicTacToeActor implements GameActor<TicTacToeState> {

    final private Random random = new Random();

    @Override
    public void move(GamePlayer<TicTacToeState> playerToMove) {
        TicTacToePlayer player = (TicTacToePlayer) playerToMove;
        // Step 1. Checking next move
        GameMove nextMove = playerToMove.getNextMove();
        if (nextMove instanceof TicTacToeSelectCellMove) {
            // Step 1.1 Select move to be made
            TicTacToeCellState[][] board = playerToMove.getState().getBoard();
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (!board[i][j].owned()) {
                        player.select(i, j);
                        return;
                    }
                }
            }
            throw new IllegalArgumentException("This action can't be performed");
        } else if (nextMove instanceof TicTacToeBetOnCellMove) {
            // Step 1.2 Bet move to be made
            if (player.getMoneyLeft() > 0) {
                player.bet(random.nextInt(player.getMoneyLeft() + 1));
            } else {
                player.bet(0);
            }
        }
    }
}
