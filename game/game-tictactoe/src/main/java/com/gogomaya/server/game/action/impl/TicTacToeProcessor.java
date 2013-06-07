package com.gogomaya.server.game.action.impl;

import java.util.Collection;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.game.action.GameOutcome;
import com.gogomaya.server.game.action.GamePlayerState;
import com.gogomaya.server.game.action.GameProcessor;
import com.gogomaya.server.game.action.GameSession;
import com.gogomaya.server.game.action.NoOutcome;
import com.gogomaya.server.game.action.PlayerWonOutcome;
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

public class TicTacToeProcessor implements GameProcessor<TicTacToeState> {

    @Override
    public Collection<GameEvent<TicTacToeState>> process(final GameSession<TicTacToeState> session, final TicTacToeState state, GameMove move) {
        // Step 1. Processing Select cell move
        if (state.complete())
            return ImmutableList.<GameEvent<TicTacToeState>> of();
        if (move instanceof TicTacToeSelectCellMove) {
            return ImmutableList.<GameEvent<TicTacToeState>> of(processSelectCellMove(state, (TicTacToeSelectCellMove) move));
        } else if (move instanceof TicTacToeBetOnCellMove) {
            return ImmutableList.<GameEvent<TicTacToeState>> of(processBetOnCellMove(state, (TicTacToeBetOnCellMove) move));
        } else if (move instanceof GiveUpMove) {
            return processGiveUpMove(state, (GiveUpMove) move);
        }
        // Step 2. Returning default state
        throw GogomayaException.create(GogomayaError.GamePlayMoveNotSupported);
    }

    private Collection<GameEvent<TicTacToeState>> processGiveUpMove(final TicTacToeState state, final GiveUpMove giveUpMove) {
        // Step 1. Fetching player identifier
        long looser = giveUpMove.getPlayerId();
        Collection<Long> opponents = state.getOpponents(looser);
        if (opponents.size() == 0 || state.getVersion() == 1) {
            // Step 2. No game started just live the table
            state.setOutcome(new NoOutcome());
            return ImmutableList.<GameEvent<TicTacToeState>> of(new PlayerGaveUpEvent<TicTacToeState>().setPlayerId(looser).setState(state),
                    new GameEndedEvent<TicTacToeState>().setOutcome(new NoOutcome()).setState(state));
        } else {
            long winner = opponents.iterator().next();
            state.setOutcome(new PlayerWonOutcome(winner));
            specifyWinner(winner, state);
            // Step 2. Player gave up, consists of 2 parts - Gave up, and Ended since there is no players involved
            return ImmutableList.<GameEvent<TicTacToeState>> of(new PlayerGaveUpEvent<TicTacToeState>().setPlayerId(looser).setState(state),
                    new GameEndedEvent<TicTacToeState>(state));
        }
    }

    private GameEvent<TicTacToeState> processBetOnCellMove(final TicTacToeState state, final TicTacToeBetOnCellMove betMove) {
        // Step 1. Populating made moves
        state.addMadeMove(betMove);
        // Step 2. Checking if everybody already made their bets
        if (state.getNextMoves().isEmpty()) {
            long[] players = state.getPlayerIterator().getPlayers();

            long firstPlayerBet = ((TicTacToeBetOnCellMove) state.getMadeMove(players[0])).getBet();
            state.getPlayerState(players[0]).subMoneyLeft(firstPlayerBet);

            long secondPlayerBet = ((TicTacToeBetOnCellMove) state.getMadeMove(players[1])).getBet();
            state.getPlayerState(players[1]).subMoneyLeft(secondPlayerBet);

            TicTacToeCellState cellState = (firstPlayerBet == secondPlayerBet) ? new TicTacToeCellState(0L, firstPlayerBet, secondPlayerBet)
                    : new TicTacToeCellState(firstPlayerBet > secondPlayerBet ? players[0] : players[1], firstPlayerBet, secondPlayerBet);
            state.setBoard(state.getActiveCell(), cellState);
            state.setActiveCell(TicTacToeCell.DEFAULT);

            GameOutcome outcome = state.getOutcome();
            if (outcome != null && state.getOutcome() instanceof PlayerWonOutcome) {
                specifyWinner(((PlayerWonOutcome) outcome).getWinner(), state);
            }

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
        state.setNextMoves(nextMoves);
        // Step 3. Returning result
        return new PlayerMovedEvent<TicTacToeState>().setMadeMove(selectCellMove).setNextMoves(nextMoves).setState(state);
    }

    private void specifyWinner(final long winner, final TicTacToeState state) {
        for (GamePlayerState playerState : state.getPlayerStates()) {
            if (playerState.getPlayerId() != winner) {
                playerState.subMoneyLeft(playerState.getMoneyLeft());
            }
        }
    }

}
