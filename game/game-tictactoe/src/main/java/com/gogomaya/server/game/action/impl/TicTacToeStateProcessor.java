package com.gogomaya.server.game.action.impl;

import java.util.Collection;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.game.action.GameProcessor;
import com.gogomaya.server.game.action.move.GameMove;
import com.gogomaya.server.game.action.move.GiveUpMove;
import com.gogomaya.server.game.event.GameEndedEvent;
import com.gogomaya.server.game.event.GameEvent;
import com.gogomaya.server.game.event.PlayerGaveUpEvent;
import com.gogomaya.server.game.event.PlayerMovedEvent;
import com.gogomaya.server.game.tictactoe.action.TicTacToeCell;
import com.gogomaya.server.game.tictactoe.action.TicTacToeCellState;
import com.gogomaya.server.game.tictactoe.action.TicTacToeState;
import com.gogomaya.server.game.tictactoe.action.move.TicTacToeBetOnCellMove;
import com.gogomaya.server.game.tictactoe.action.move.TicTacToeSelectCellMove;
import com.google.common.collect.ImmutableList;

public class TicTacToeStateProcessor implements GameProcessor<TicTacToeState>{

    @Override
    public Collection<GameEvent<TicTacToeState>> process(TicTacToeState state, GameMove move) {
        // Step 1. Processing Select cell move
        if (move instanceof TicTacToeSelectCellMove) {
            return ImmutableList.<GameEvent<TicTacToeState>>of(processSelectCellMove(state, (TicTacToeSelectCellMove) move));
        } else if (move instanceof TicTacToeBetOnCellMove) {
            return ImmutableList.<GameEvent<TicTacToeState>>of(processBetOnCellMove(state, (TicTacToeBetOnCellMove) move));
        } else if (move instanceof GiveUpMove) {
            return processGiveUpMove(state, (GiveUpMove) move);
        }
        // Step 2. Returning default state
        throw GogomayaException.create(GogomayaError.GamePlayMoveNotSupported);
    }

    private Collection<GameEvent<TicTacToeState>> processGiveUpMove(final TicTacToeState state, final GiveUpMove giveUpMove) {
        // Step 1. Fetching player identifier
        long playerId = giveUpMove.getPlayerId();
        // Step 2. Player gave up, consists of 2 parts - Gave up, and Ended since there is no players involved
        return ImmutableList.<GameEvent<TicTacToeState>> of(
                new PlayerGaveUpEvent<TicTacToeState>().setPlayerId(playerId).setState(state),
                new GameEndedEvent<TicTacToeState>().setPlayerId(playerId).setState(state));
    }

    private GameEvent<TicTacToeState> processBetOnCellMove(final TicTacToeState state, final TicTacToeBetOnCellMove betMove) {
        state.getPlayerState(betMove.getPlayerId()).subMoneyLeft(betMove.getBet());
        state.addMadeMove(betMove);

        if (state.getNextMoves().isEmpty()) {
            long[] players = state.getPlayerIterator().getPlayers();

            long firstPlayerBet = ((TicTacToeBetOnCellMove) state.getMadeMove(players[0])).getBet();
            long secondPlayerBet = ((TicTacToeBetOnCellMove) state.getMadeMove(players[1])).getBet();

            state.getBoard()[state.getActiveCell().getRow()][state.getActiveCell().getColumn()] = (firstPlayerBet == secondPlayerBet) ? new TicTacToeCellState(
                    0L, firstPlayerBet, secondPlayerBet) : new TicTacToeCellState(firstPlayerBet > secondPlayerBet ? players[0] : players[1], firstPlayerBet,
                    secondPlayerBet);
            state.setActiveCell(TicTacToeCell.DEFAULT);

            state.setNextMove(new TicTacToeSelectCellMove(state.getPlayerIterator().next()));
            state.cleanMadeMove();
        }

        return new PlayerMovedEvent<TicTacToeState>().setMadeMove(betMove).setNextMoves(state.getNextMoves()).setState(state);
    }

    private GameEvent<TicTacToeState> processSelectCellMove(final TicTacToeState state, final TicTacToeSelectCellMove selectCellMove) {
        TicTacToeCell cellToSelect = selectCellMove.getCell();
        // Step 1. Sanity check
        if (state.getBoard()[cellToSelect.getRow()][cellToSelect.getColumn()].owned()) {
            throw GogomayaException.create(GogomayaError.TicTacToeCellOwned);
        }
        // Step 2. Generating next moves
        state.setActiveCell(cellToSelect);
        state.cleanMadeMove();
        long[] players = state.getPlayerIterator().getPlayers();
        Collection<GameMove> nextMoves = (ImmutableList.<GameMove> of(new TicTacToeBetOnCellMove(players[0]), new TicTacToeBetOnCellMove(players[1])));
        // Step 3. Returning result
        return new PlayerMovedEvent<TicTacToeState>().setMadeMove(selectCellMove).setNextMoves(nextMoves).setState(state);
    }

}
