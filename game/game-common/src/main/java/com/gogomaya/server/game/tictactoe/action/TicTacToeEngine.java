package com.gogomaya.server.game.tictactoe.action;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
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
        throw GogomayaException.create(GogomayaError.GamePlayMoveNotSupported);
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

            TicTacToeCellState activeCellState = (firstPlayerBet == secondPlayerBet) 
                    ? new TicTacToeCellState(0L, firstPlayerBet, secondPlayerBet)
                    : new TicTacToeCellState(firstPlayerBet > secondPlayerBet ? players[0] : players[1], firstPlayerBet, secondPlayerBet);
            oldState.setActiveCellState(activeCellState);

            oldState.setNextMoveSelect(oldState.getPlayerIterator().next());
        }

        return oldState;
    }

    private TicTacToeState processSelectCellMove(final TicTacToeState oldState, final TicTacToeSelectCellMove selectCellMove) {
        // Step 1. Sanity check
        if (oldState.isOwned(selectCellMove.getCell())) {
            throw GogomayaException.create(GogomayaError.TicTacToeCellOwned);
        }
        // Step 2. Generating next moves
        oldState.setNextMoveBet();
        oldState.setActiveCell(selectCellMove.getCell());
        // Step 3. Returning modified old state
        return oldState;
    }

}
