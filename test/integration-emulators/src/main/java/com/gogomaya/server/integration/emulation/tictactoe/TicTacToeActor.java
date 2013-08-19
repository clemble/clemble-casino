package com.gogomaya.server.integration.emulation.tictactoe;

import java.util.Random;

import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.Game;
import com.gogomaya.server.game.cell.CellState;
import com.gogomaya.server.game.event.client.BetEvent;
import com.gogomaya.server.game.event.client.generic.SelectCellEvent;
import com.gogomaya.server.integration.emulator.GameActor;
import com.gogomaya.server.integration.game.GameSessionPlayer;
import com.gogomaya.server.integration.game.tictactoe.TicTacToeSessionPlayer;
import com.gogomaya.server.tictactoe.TicTacToeState;

public class TicTacToeActor implements GameActor<TicTacToeState> {

    /**
     * Generated 13/07/13
     */
    private static final long serialVersionUID = 8438692609799609799L;

    final private Random random = new Random();

    @Override
    public Game getGame() {
        return Game.pic;
    }

    @Override
    public void move(GameSessionPlayer<TicTacToeState> playerToMove) {
        TicTacToeSessionPlayer player = (TicTacToeSessionPlayer) playerToMove;
        // Step 1. Checking next move
        ClientEvent nextMove = playerToMove.getNextMove();
        if (nextMove instanceof SelectCellEvent) {
            // Step 1.1 Select move to be made
            CellState[][] board = playerToMove.getState().getBoard();
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (!board[i][j].owned()) {
                        player.select(i, j);
                        return;
                    }
                }
            }
            throw new IllegalArgumentException("This action can't be performed");
        } else if (nextMove instanceof BetEvent) {
            // Step 1.2 Bet move to be made
            if (player.getMoneySpent() > 0) {
                player.bet(random.nextInt(player.getMoneySpent() + 1));
            } else {
                player.bet(0);
            }
        }
    }

}
