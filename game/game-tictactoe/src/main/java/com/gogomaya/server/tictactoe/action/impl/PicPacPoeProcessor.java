package com.gogomaya.server.tictactoe.action.impl;

import java.util.Collection;

import com.gogomaya.server.ActionLatch;
import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.GameAccount;
import com.gogomaya.server.game.GamePlayerIterator;
import com.gogomaya.server.game.GameState;
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
import com.gogomaya.server.tictactoe.PicPacPoeState;
import com.google.common.collect.ImmutableList;

public class PicPacPoeProcessor implements GameState {

    /**
     * 
     */
    private static final long serialVersionUID = -755717572685487667L;

    private GameAccount account;
    private GamePlayerIterator playerIterator;
    private ActionLatch actionLatch;
    private GameOutcome outcome;
    private int version;

    private PicPacPoeState picPacPoeState;

    public PicPacPoeProcessor(GameAccount account,
            PicPacPoeState picPacPoeState,
            GamePlayerIterator playerIterator,
            ActionLatch actionLatch,
            GameOutcome outcome,
            int version) {
        this.account = account;
        this.outcome = outcome;
        this.actionLatch = actionLatch;
        this.playerIterator = playerIterator;
        this.picPacPoeState = picPacPoeState;
        this.version = version;
    }

    public Collection<GameServerEvent<PicPacPoeProcessor>> process(ClientEvent clientEvent) {
        // Step 1. Processing Select cell move
        if (outcome != null) {
            return ImmutableList.<GameServerEvent<PicPacPoeProcessor>> of();
        }
        if (clientEvent instanceof SelectCellEvent) {
            Cell cellToSelect = ((SelectCellEvent) clientEvent).getCell();
            // Step 1. Sanity check
            if (picPacPoeState.getBoard()[cellToSelect.getRow()][cellToSelect.getColumn()].owned()) {
                throw GogomayaException.fromError(GogomayaError.TicTacToeCellOwned);
            }
            // Step 2. Generating next moves
            picPacPoeState.setSelected(cellToSelect);
            setBetNext();
            // Step 3. Returning result
            return ImmutableList.<GameServerEvent<PicPacPoeProcessor>> of(new PlayerMovedEvent<PicPacPoeProcessor>().setMadeMove(clientEvent).setState(this));
        } else if (clientEvent instanceof BetEvent) {
            // Step 1. Populating made moves
            actionLatch.put(clientEvent.getPlayerId(), (BetEvent) clientEvent);
            // Step 2. Checking if everybody already made their bets
            if (actionLatch.complete()) {
                // Step 1. Reducing account ammounts
                Collection<BetEvent> bets = actionLatch.getActions();
                for (BetEvent bet : bets) {
                    account.subMoneyLeft(bet.getPlayerId(), bet.getBet());
                }
                // Step 2. Setting exposed cell state
                picPacPoeState.setSelectedState(new ExposedCellState(bets));
                // Step 3. Checking if PicPacPoe is over
                outcome = PicPacPoeState.fetchOutcome(picPacPoeState);

                setSelectNext();
            } else {
                this.version++;
            }

            return ImmutableList.<GameServerEvent<PicPacPoeProcessor>> of(new PlayerMovedEvent<PicPacPoeProcessor>().setMadeMove(clientEvent).setState(this));
        } else if (clientEvent instanceof SurrenderEvent) {
            // Step 1. Fetching player identifier
            long looser = ((SurrenderEvent) clientEvent).getPlayerId();
            Collection<Long> opponents = playerIterator.whoIsOpponents(looser);
            if (opponents.size() == 0 || version == 1) {
                // Step 2. No game started just live the table
                outcome = new NoOutcome();
                return ImmutableList.<GameServerEvent<PicPacPoeProcessor>> of(new PlayerLostEvent<PicPacPoeProcessor>().setPlayerId(looser).setState(this),
                        new GameEndedEvent<PicPacPoeProcessor>().setOutcome(new NoOutcome()).setState(this));
            } else {
                long winner = opponents.iterator().next();
                outcome = new PlayerWonOutcome(winner);
                // Step 2. Player gave up, consists of 2 parts - Gave up, and Ended since there is no players involved
                return ImmutableList.<GameServerEvent<PicPacPoeProcessor>> of(new PlayerLostEvent<PicPacPoeProcessor>().setReason((SurrenderEvent) clientEvent)
                        .setPlayerId(looser).setState(this), new GameEndedEvent<PicPacPoeProcessor>(this));
            }
        }
        // Step 2. Returning default state
        throw GogomayaException.fromError(GogomayaError.GamePlayMoveNotSupported);
    }

    final public GameState setSelectNext() {
        this.actionLatch = new ActionLatch(playerIterator.next(), "select", SelectCellEvent.class);
        this.version++;
        return this;
    }

    final public GameState setBetNext() {
        this.actionLatch = new ActionLatch(playerIterator.getPlayers(), "bet", BetEvent.class);
        this.version++;
        return this;
    }

    @Override
    public GameAccount getAccount() {
        return account;
    }

    @Override
    public GamePlayerIterator getPlayerIterator() {
        return playerIterator;
    }

    @Override
    public ActionLatch getActionLatch() {
        return actionLatch;
    }

    @Override
    public GameOutcome getOutcome() {
        return outcome;
    }

    @Override
    public int getVersion() {
        return version;
    }
}
