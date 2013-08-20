package com.gogomaya.server.tictactoe.action.impl;

import java.util.Collection;

import com.gogomaya.server.ActionLatch;
import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.GamePlayerState;
import com.gogomaya.server.game.GameSession;
import com.gogomaya.server.game.action.GameProcessor;
import com.gogomaya.server.game.cell.Cell;
import com.gogomaya.server.game.cell.ExposedCellState;
import com.gogomaya.server.game.event.client.BetEvent;
import com.gogomaya.server.game.event.client.generic.SelectCellEvent;
import com.gogomaya.server.game.event.client.surrender.SurrenderEvent;
import com.gogomaya.server.game.event.server.GameEndedEvent;
import com.gogomaya.server.game.event.server.GameServerEvent;
import com.gogomaya.server.game.event.server.PlayerLostEvent;
import com.gogomaya.server.game.event.server.PlayerMovedEvent;
import com.gogomaya.server.game.outcome.GameOutcome;
import com.gogomaya.server.game.outcome.NoOutcome;
import com.gogomaya.server.game.outcome.PlayerWonOutcome;
import com.gogomaya.server.tictactoe.TicTacToeState;
import com.google.common.collect.ImmutableList;

public class TicTacToeProcessor implements GameProcessor<TicTacToeState> {

    @Override
    public Collection<GameServerEvent<TicTacToeState>> process(final GameSession<TicTacToeState> session, ClientEvent clientEvent) {
        TicTacToeState state = session.getState();
        // Step 1. Processing Select cell move
        if (state.getOutcome() != null) {
            return ImmutableList.<GameServerEvent<TicTacToeState>> of();
        } else if (clientEvent instanceof SelectCellEvent) {
            return ImmutableList.<GameServerEvent<TicTacToeState>> of(processSelectCellEvent(state, (SelectCellEvent) clientEvent));
        } else if (clientEvent instanceof BetEvent) {
            return ImmutableList.<GameServerEvent<TicTacToeState>> of(processBetOnCellEvent(state, (BetEvent) clientEvent));
        } else if (clientEvent instanceof SurrenderEvent) {
            return processSurrenderEvent(state, (SurrenderEvent) clientEvent);
        }
        // Step 2. Returning default state
        throw GogomayaException.fromError(GogomayaError.GamePlayMoveNotSupported);
    }

    private Collection<GameServerEvent<TicTacToeState>> processSurrenderEvent(final TicTacToeState state, final SurrenderEvent surrenderEvent) {
        // Step 1. Fetching player identifier
        long looser = surrenderEvent.getPlayerId();
        Collection<Long> opponents = state.getOpponents(looser);
        if (opponents.size() == 0 || state.getVersion() == 1) {
            // Step 2. No game started just live the table
            state.setOutcome(new NoOutcome());
            return ImmutableList.<GameServerEvent<TicTacToeState>> of(new PlayerLostEvent<TicTacToeState>().setPlayerId(looser).setState(state),
                    new GameEndedEvent<TicTacToeState>().setOutcome(new NoOutcome()).setState(state));
        } else {
            long winner = opponents.iterator().next();
            state.setOutcome(new PlayerWonOutcome(winner));
            specifyWinner(winner, state);
            // Step 2. Player gave up, consists of 2 parts - Gave up, and Ended since there is no players involved
            return ImmutableList.<GameServerEvent<TicTacToeState>> of(new PlayerLostEvent<TicTacToeState>().setReason(surrenderEvent).setPlayerId(looser)
                    .setState(state), new GameEndedEvent<TicTacToeState>(state));
        }
    }

    private GameServerEvent<TicTacToeState> processBetOnCellEvent(final TicTacToeState state, final BetEvent betMove) {
        // Step 1. Populating made moves
        state.addMadeMove(betMove);
        // Step 2. Checking if everybody already made their bets
        ActionLatch actionLatch = state.getActionLatch();
        if (actionLatch.complete()) {
            // Step 1. Reducing account ammounts
            Collection<BetEvent> bets = actionLatch.getActions();
            for(BetEvent bet : bets) {
                state.getPlayerState(bet.getPlayerId()).subMoneyLeft(bet.getBet());
            }
            // Step 2. Setting exposed cell state
            ExposedCellState cellState = new ExposedCellState(bets);
            state.setSelectedState(cellState);
            // Step 3. 
            GameOutcome outcome = state.getOutcome();
            if (outcome != null && state.getOutcome() instanceof PlayerWonOutcome) {
                specifyWinner(((PlayerWonOutcome) outcome).getWinner(), state);
            }

            state.setSelectNext();
        }

        return new PlayerMovedEvent<TicTacToeState>().setMadeMove(betMove).setState(state);
    }

    private GameServerEvent<TicTacToeState> processSelectCellEvent(final TicTacToeState state, final SelectCellEvent selectCellMove) {
        state.addMadeMove(selectCellMove);
        Cell cellToSelect = selectCellMove.getCell();
        // Step 1. Sanity check
        if (state.getBoard()[cellToSelect.getRow()][cellToSelect.getColumn()].owned()) {
            throw GogomayaException.fromError(GogomayaError.TicTacToeCellOwned);
        }
        // Step 2. Generating next moves
        state.setSelected(cellToSelect);
        state.setBetNext();
        // Step 3. Returning result
        return new PlayerMovedEvent<TicTacToeState>().setMadeMove(selectCellMove).setState(state);
    }

    private void specifyWinner(final long winner, final TicTacToeState state) {
        for (GamePlayerState playerState : state.getPlayerStates()) {
            if (playerState.getPlayerId() != winner) {
                playerState.subMoneyLeft(playerState.getMoneyLeft());
            }
        }
    }

}
