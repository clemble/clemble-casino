package com.gogomaya.server.game.tictactoe.action;

import java.util.Collection;

import com.gogomaya.server.game.action.impl.AbstractGameEngine;
import com.gogomaya.server.game.tictactoe.action.move.TicTacToeBetOnCellMove;
import com.gogomaya.server.game.tictactoe.action.move.TicTacToeMove;
import com.gogomaya.server.game.tictactoe.action.move.TicTacToeSelectCellMove;
import com.google.common.collect.ImmutableList;

public class TicTacToeEngine extends AbstractGameEngine<TicTacToeState, TicTacToeMove, TicTacToePlayerState> {

    @Override
    final protected TicTacToeState safeProcess(final TicTacToeState oldState, final TicTacToeMove move) {
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

        long[] players = oldState.getPlayerIterator().getPlayers();
        TicTacToeBetOnCellMove firstPlayerMove = (TicTacToeBetOnCellMove) oldState.getMadeMove(players[0]);
        TicTacToeBetOnCellMove secondPlayerMove = (TicTacToeBetOnCellMove) oldState.getMadeMove(players[1]);
        if (firstPlayerMove != null && secondPlayerMove != null) {
            long firstPlayerBet = firstPlayerMove.getBet();
            long secondPlayerBet = secondPlayerMove.getBet();

            long nextPlayer = firstPlayerBet == secondPlayerBet ? oldState.getPlayerIterator().current() : oldState.getPlayerIterator().next();

            if (firstPlayerBet == secondPlayerBet) {
                oldState.setActiveCellState(new TicTacToeCellState(0L, firstPlayerBet, secondPlayerBet));
            } else {
                oldState.setActiveCellState(new TicTacToeCellState(firstPlayerBet > secondPlayerBet ? players[0] : players[1], firstPlayerBet, secondPlayerBet));
            }

            oldState.setNextMove(new TicTacToeSelectCellMove(nextPlayer));
            oldState.setActiveCell(null);
            oldState.cleanMadeMove();
        }

        return oldState;
    }

    private TicTacToeState processSelectCellMove(final TicTacToeState oldState, final TicTacToeSelectCellMove selectCellMove) {
        // Step 1. Sanity check
        if (oldState.isOwned(selectCellMove.getCell()))
            return oldState;
        // Step 2. Generating next moves
        Collection<TicTacToeMove> nextMoves = ImmutableList.<TicTacToeMove> of(new TicTacToeBetOnCellMove(oldState.getPlayerIterator().getPlayers()[0]),
                new TicTacToeBetOnCellMove(oldState.getPlayerIterator().getPlayers()[1]));
        oldState.setNextMoves(nextMoves);
        oldState.setActiveCell(selectCellMove.getCell());
        oldState.cleanMadeMove();
        // Step 3. Returning modified old state
        return oldState;
    }

}
