package com.gogomaya.server.tictactoe;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gogomaya.server.ActionLatch;
import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.GameAccount;
import com.gogomaya.server.game.GamePlayerIterator;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.event.client.BetEvent;
import com.gogomaya.server.game.event.client.generic.SelectCellEvent;
import com.gogomaya.server.game.outcome.GameOutcome;

@JsonIgnoreProperties(value = { "activeUsers" })
abstract public class AbstractPicPacPoeGameState implements GameState {

    /**
     * Generated 02/04/13
     */
    private static final long serialVersionUID = -6468020813755923981L;

    private GameAccount gameAccount;

    @JsonIgnore
    private GamePlayerIterator playerIterator;

    private ActionLatch actionLatch;

    private GameOutcome outcome;

    @JsonProperty("version")
    private int version;

    @Override
    public GameAccount getAccount() {
        return gameAccount;
    }

    public void setAccount(GameAccount gameAccount) {
        this.gameAccount = gameAccount;
    }

    final public Set<Long> getOpponents(long playerId) {
        // Step 1. Calculating opponents from the original list
        Set<Long> opponents = new HashSet<Long>();
        for (long opponent : playerIterator.getPlayers()) {
            if (opponent != playerId) {
                opponents.add(opponent);
            }
        }
        // Step 2. Returning list of opponents
        return opponents;
    }

    public ActionLatch getActionLatch() {
        return actionLatch;
    }

    public void setActionLatch(ActionLatch actionLatch) {
        this.actionLatch = actionLatch;
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

    final public GameState addMadeMove(ClientEvent playerMove) {
        this.actionLatch.put(playerMove.getPlayerId(), playerMove);
        this.version++;
        return this;
    }

    @Override
    final public GamePlayerIterator getPlayerIterator() {
        return playerIterator;
    }

    @Override
    final public GameState setPlayerIterator(GamePlayerIterator playerIterator) {
        this.playerIterator = playerIterator;
        return this;
    }

    abstract public GameOutcome calculate();

    @Override
    public GameOutcome getOutcome() {
        // Step 1. If we already calculated outcome return it
        if (outcome != null)
            return outcome;
        // Step 2. Checking if there is a single winner
        this.outcome = calculate();
        // Step 3. Returning GameOutcome
        return outcome;
    }

    public GameState setOutcome(GameOutcome outcome) {
        if (this.outcome != null)
            throw GogomayaException.fromError(GogomayaError.ServerCriticalError);
        this.outcome = outcome;
        this.version++;
        return this;
    }

    @Override
    final public int getVersion() {
        return version;
    }

    final public void setVersion(int version) {
        this.version = version;
    }

}
