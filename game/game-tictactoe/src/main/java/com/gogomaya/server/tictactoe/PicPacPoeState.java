package com.gogomaya.server.tictactoe;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
import com.gogomaya.server.game.event.server.PlayerMovedEvent;
import com.gogomaya.server.game.outcome.GameOutcome;
import com.gogomaya.server.game.outcome.NoOutcome;
import com.gogomaya.server.game.outcome.PlayerWonOutcome;

@JsonTypeName("picPacPoe")
public class PicPacPoeState implements GameState {

    /**
     * 
     */
    private static final long serialVersionUID = -755717572685487667L;

    private GameAccount account;
    private GamePlayerIterator playerIterator;
    private ActionLatch actionLatch;
    private GameOutcome outcome;
    private int version;

    private PicPacPoeBoard board;

    public PicPacPoeState(GameAccount account, GamePlayerIterator playerIterator) {
        this(account, new PicPacPoeBoard(), playerIterator, new ActionLatch(playerIterator.current(), "select"), null, 0);
    }

    @JsonCreator
    public PicPacPoeState(@JsonProperty("account") GameAccount account,
            @JsonProperty("board") PicPacPoeBoard picPacPoeState,
            @JsonProperty("iterator") GamePlayerIterator playerIterator,
            @JsonProperty("actionLatch") ActionLatch actionLatch,
            @JsonProperty("outcome") GameOutcome outcome,
            @JsonProperty("version") int version) {
        this.account = account;
        this.outcome = outcome;
        this.actionLatch = actionLatch;
        this.playerIterator = playerIterator;
        this.board = picPacPoeState;
        this.version = version;
    }

    @Override
    public GameServerEvent<GameState> process(ClientEvent clientEvent) {
        // Step 1. Processing Select cell move
        if (outcome != null) {
            throw GogomayaException.fromError(GogomayaError.GamePlayGameEnded);
        }

        GameServerEvent<GameState> resultEvent = null;

        if (clientEvent instanceof SelectCellEvent) {
            actionLatch.put(clientEvent.getPlayerId(), clientEvent);
            Cell cellToSelect = ((SelectCellEvent) clientEvent).getCell();
            // Step 1. Sanity check
            if (board.getBoard()[cellToSelect.getRow()][cellToSelect.getColumn()].owned()) {
                throw GogomayaException.fromError(GogomayaError.TicTacToeCellOwned);
            }
            // Step 2. Generating next moves
            board.setSelected(cellToSelect);
            actionLatch = new ActionLatch(playerIterator.getPlayers(), "bet", BetEvent.class);
            // Step 3. Returning result
            resultEvent = new PlayerMovedEvent<GameState>().setMadeMove(clientEvent).setState(this);
        } else if (clientEvent instanceof BetEvent) {
            // Step 1. Populating made moves
            actionLatch.put(clientEvent.getPlayerId(), (BetEvent) clientEvent);
            // Step 2. Checking if everybody already made their bets
            if (actionLatch.complete()) {
                // Step 1. Reducing account ammounts
                Collection<BetEvent> bets = this.actionLatch.getActions();
                for (BetEvent bet : bets) {
                    this.account.subMoneyLeft(bet.getPlayerId(), bet.getBet());
                }
                // Step 2. Setting exposed cell state
                board.setSelectedState(new ExposedCellState(bets));
                // Step 3. Checking if PicPacPoe is over
                outcome = PicPacPoeBoard.fetchOutcome(board);

                actionLatch = new ActionLatch(playerIterator.next(), "select", SelectCellEvent.class);
            }
            resultEvent = new PlayerMovedEvent<GameState>().setMadeMove(clientEvent).setState(this);
        } else if (clientEvent instanceof SurrenderEvent) {
            // Step 1. Fetching player identifier
            long looser = ((SurrenderEvent) clientEvent).getPlayerId();
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

    public PicPacPoeBoard getBoard() {
        return board;
    }

    public void setBoard(PicPacPoeBoard board) {
        this.board = board;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((account == null) ? 0 : account.hashCode());
        result = prime * result + ((actionLatch == null) ? 0 : actionLatch.hashCode());
        result = prime * result + ((board == null) ? 0 : board.hashCode());
        result = prime * result + ((outcome == null) ? 0 : outcome.hashCode());
        result = prime * result + ((playerIterator == null) ? 0 : playerIterator.hashCode());
        result = prime * result + version;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PicPacPoeState other = (PicPacPoeState) obj;
        if (account == null) {
            if (other.account != null)
                return false;
        } else if (!account.equals(other.account))
            return false;
        if (actionLatch == null) {
            if (other.actionLatch != null)
                return false;
        } else if (!actionLatch.equals(other.actionLatch))
            return false;
        if (board == null) {
            if (other.board != null)
                return false;
        } else if (!board.equals(other.board))
            return false;
        if (outcome == null) {
            if (other.outcome != null)
                return false;
        } else if (!outcome.equals(other.outcome))
            return false;
        if (playerIterator == null) {
            if (other.playerIterator != null)
                return false;
        } else if (!playerIterator.equals(other.playerIterator))
            return false;
        if (version != other.version)
            return false;
        return true;
    }
}
