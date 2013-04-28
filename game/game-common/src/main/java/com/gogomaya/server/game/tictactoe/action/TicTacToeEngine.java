package com.gogomaya.server.game.tictactoe.action;

import com.gogomaya.server.game.action.impl.AbstractGameEngine;
import com.gogomaya.server.game.action.move.GameMove;
import com.gogomaya.server.game.tictactoe.action.move.TicTacToeBetOnCellMove;
import com.gogomaya.server.game.tictactoe.action.move.TicTacToeSelectCellMove;

public class TicTacToeEngine extends AbstractGameEngine<TicTacToeState> {

    @Override
    final protected TicTacToeState safeProcess(final TicTacToeState oldState, final GameMove move) {
        // Step 1. Processing Select cell move
        if (move instanceof TicTacToeSelectCellMove) {
            return processSelectCellMove(oldState, (TicTacToeSelectCellMove) move);
        } else if (move instanceof TicTacToeBetOnCellMove) {
            return processBetOnCellMove(oldState, (TicTacToeBetOnCellMove) move);
        }
        // Step 2. Returning default state
        return oldState;
    }

    private TicTacToeState processBetOnCellMove(final TicTacToeState oldState, final TicTacToeBetOnCellMove betMove) {
        oldState.getPlayerState(betMove.getPlayerId()).subMoneyLeft(betMove.getBet());
        oldState.addMadeMove(betMove);

        if (oldState.getNextMoves().isEmpty()) {
            long[] players = oldState.getPlayerIterator().getPlayers();
            TicTacToeBetOnCellMove firstPlayerMove = (TicTacToeBetOnCellMove) oldState.getMadeMove(players[0]);
            TicTacToeBetOnCellMove secondPlayerMove = (TicTacToeBetOnCellMove) oldState.getMadeMove(players[1]);

            long firstPlayerBet = firstPlayerMove.getBet();
            long secondPlayerBet = secondPlayerMove.getBet();

            if (firstPlayerBet == secondPlayerBet) {
                oldState.setActiveCellState(new TicTacToeCellState(0L, firstPlayerBet, secondPlayerBet));
            } else {
                oldState.setActiveCellState(new TicTacToeCellState(firstPlayerBet > secondPlayerBet ? players[0] : players[1], firstPlayerBet, secondPlayerBet));
            }

            oldState.setNextMoveSelect(oldState.getPlayerIterator().next());
            oldState.setActiveCell(TicTacToeCell.DEFAULT);
            oldState.cleanMadeMove();
        }

        return oldState;
    }

    private TicTacToeState processSelectCellMove(final TicTacToeState oldState, final TicTacToeSelectCellMove selectCellMove) {
        // Step 1. Sanity check
        if (oldState.isOwned(selectCellMove.getCell())) {
            throw new IllegalArgumentException("Cell " + selectCellMove.getCell() + " owned by " + oldState.getCellState(selectCellMove.getCell()));
        }
        // Step 2. Generating next moves
        oldState.setNextMoveBet();
        oldState.setActiveCell(selectCellMove.getCell());
        oldState.cleanMadeMove();
        // Step 3. Returning modified old state
        return oldState;
    }

}
