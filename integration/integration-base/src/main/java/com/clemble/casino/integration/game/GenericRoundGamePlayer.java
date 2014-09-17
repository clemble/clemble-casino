package com.clemble.casino.integration.game;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.List;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.event.Event;
import com.clemble.casino.game.GameRecord;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.event.GameEvent;
import com.clemble.casino.game.outcome.GameOutcome;
import com.clemble.casino.game.configuration.GameConfiguration;

public class GenericRoundGamePlayer<State extends GameState> implements RoundGamePlayer<State> {

    /**
     * Generated 05/07/13
     */
    private static final long serialVersionUID = -4604087499745502553L;

    final protected RoundGamePlayer<State> actualPlayer;

    public GenericRoundGamePlayer(RoundGamePlayer<State> delegate) {
        this.actualPlayer = checkNotNull(delegate);
    }

    @Override
    public String getPlayer(){
        return actualPlayer.getPlayer();
    }

    @Override
    public ClembleCasinoOperations playerOperations() {
        return actualPlayer.playerOperations();
    }

    @Override
    final public String getSessionKey() {
        return actualPlayer.getSessionKey();
    }

    @Override
    final public GameConfiguration getConfiguration() {
        return actualPlayer.getConfiguration();
    }

    @Override
    final public State getState() {
        return actualPlayer.getState();
    }

    @Override
    final public boolean isAlive() {
        return actualPlayer.isAlive();
    }

    @Override
    final public GamePlayer waitForStart() {
        actualPlayer.waitForStart();
        return this;
    }

    @Override
    final public GamePlayer waitForStart(long timeout) {
        actualPlayer.waitForStart(timeout);
        return this;
    }

    @Override
    public GamePlayer waitForEnd() {
        actualPlayer.waitForEnd();
        return this;
    }

    @Override
    public GamePlayer waitForEnd(long timeout) {
        actualPlayer.waitForEnd(timeout);
        return this;
    }

    @Override
    final public void waitForTurn() {
        actualPlayer.waitForTurn();
    }

    @Override
    final public boolean isToMove() {
        return actualPlayer.isToMove();
    }

    @Override
    final public Event getNextMove() {
        return actualPlayer.getNextMove();
    }

    @Override
    final public void perform(Event gameAction) {
        actualPlayer.perform(gameAction);
    }

    @Override
    final public GamePlayer giveUp() {
        actualPlayer.giveUp();
        return this;
    }

    @Override
    final public void close() {
        actualPlayer.close();
    }

    @Override
    public GameRecord getRecord() {
        return actualPlayer.getRecord();
    }

    @Override
    public int getVersion() {
        return actualPlayer.getVersion();
    }

    @Override
    public GamePlayer waitVersion(int version) {
        actualPlayer.waitVersion(version);
        return this;
    }

    @Override
    public GameOutcome getOutcome() {
        return actualPlayer.getOutcome();
    }

    @Override
    public List<GameEvent> getEvents() {
        return actualPlayer.getEvents();
    }

    @Override
    public GamePlayer addDependent(GamePlayer dependent) {
        actualPlayer.addDependent(dependent);
        return this;
    }

    @Override
    public GamePlayer addDependent(Collection<? extends GamePlayer> dependent) {
        actualPlayer.addDependent(dependent);
        return this;
    }

    @Override
    public GamePlayer syncWith(GamePlayer anotherState) {
        actualPlayer.syncWith(anotherState);
        return this;
    }

}
