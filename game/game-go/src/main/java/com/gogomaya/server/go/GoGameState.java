package com.gogomaya.server.go;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gogomaya.server.ActionLatch;
import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.GamePlayerIterator;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.bank.GameBank;
import com.gogomaya.server.game.cell.Cell;
import com.gogomaya.server.game.event.client.generic.SelectCellEvent;
import com.gogomaya.server.game.event.client.surrender.SurrenderEvent;
import com.gogomaya.server.game.event.server.GameEndedEvent;
import com.gogomaya.server.game.event.server.GameServerEvent;
import com.gogomaya.server.game.event.server.PlayerMovedEvent;
import com.gogomaya.server.game.outcome.GameOutcome;
import com.gogomaya.server.game.outcome.NoOutcome;
import com.gogomaya.server.game.outcome.PlayerWonOutcome;

public class GoGameState implements GameState {

    /**
     * 
     */
    private static final long serialVersionUID = -8322660522348015422L;

    private GameBank account;
    private GamePlayerIterator playerIterator;
    private ActionLatch actionLatch;
    private GameOutcome outcome;
    private int version;

    private GoBoard board;

    public GoGameState(GameBank account, GamePlayerIterator playerIterator) {
        this(account, new GoBoard(10), playerIterator, new ActionLatch(playerIterator.current(), "select"), null, 0);
    }

    @JsonCreator
    public GoGameState(@JsonProperty("account") GameBank account,
            @JsonProperty("board") GoBoard goBoard,
            @JsonProperty("iterator") GamePlayerIterator playerIterator,
            @JsonProperty("actionLatch") ActionLatch actionLatch,
            @JsonProperty("outcome") GameOutcome outcome,
            @JsonProperty("version") int version) {
        this.account = account;
        this.board = goBoard;
        this.playerIterator = playerIterator;
        this.actionLatch = actionLatch;
        this.outcome = outcome;
        this.version = version;
    }

    @Override
    public GameBank getAccount() {
        return account;
    }

    @Override
    public GamePlayerIterator getPlayerIterator() {
        return playerIterator;
    }

    @Override
    public GameOutcome getOutcome() {
        return outcome;
    }

    @Override
    public ActionLatch getActionLatch() {
        return actionLatch;
    }

    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public GameServerEvent<GameState> process(ClientEvent move) {
        // Step 1. Processing Select cell move
        if (outcome != null) {
            throw GogomayaException.fromError(GogomayaError.GamePlayGameEnded);
        }

        GameServerEvent<GameState> resultEvent = null;

        if (move instanceof SelectCellEvent) {
            actionLatch.put(move.getPlayerId(), move);
            Cell cellToSelect = ((SelectCellEvent) move).getCell();
            // Step 1. Sanity check
            if (board.getBoard()[cellToSelect.getRow()][cellToSelect.getColumn()].owned()) {
                throw GogomayaException.fromError(GogomayaError.CellOwned);
            }
            // Step 2. Generating next moves
            board.setOwned(move.getPlayerId(), cellToSelect);
            actionLatch = new ActionLatch(playerIterator.next(), "select", SelectCellEvent.class);
            // Step 3. Returning result
            resultEvent = new PlayerMovedEvent<GameState>().setMadeMove(move).setState(this);
        } else if (move instanceof SurrenderEvent) {
            // Step 1. Fetching player identifier
            long looser = ((SurrenderEvent) move).getPlayerId();
            Collection<Long> opponents = playerIterator.whoIsOpponents(looser);
            if (opponents.size() == 0 || version == 1) {
                // Step 2. No game started just live the table
                outcome = new NoOutcome();
                resultEvent = new GameEndedEvent<GameState>(this).setOutcome(outcome);
            } else {
                long winner = opponents.iterator().next();
                outcome = new PlayerWonOutcome(winner);
                // Step 2. Player gave up, consists of 2 parts - Gave up, and Ended since there is no players involved
                resultEvent = new GameEndedEvent<GameState>(this).setOutcome(outcome);
            }
        }
        // Step 3. Sanity check
        if (resultEvent == null)
            throw GogomayaException.fromError(GogomayaError.GamePlayMoveNotSupported);
        // Step 4. Updating version and returning event
        version++;
        return resultEvent;
    }

}
