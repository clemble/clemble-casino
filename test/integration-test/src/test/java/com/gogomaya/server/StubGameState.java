package com.gogomaya.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.account.GameAccount;
import com.gogomaya.server.game.event.server.GameServerEvent;
import com.gogomaya.server.game.iterator.GamePlayerIterator;
import com.gogomaya.server.game.iterator.SequentialPlayerIterator;
import com.gogomaya.server.game.outcome.GameOutcome;
import com.google.common.collect.ImmutableList;

@JsonTypeName("stub")
@JsonIgnoreProperties("playerIterator")
public class StubGameState implements GameState {

    /**
     * 
     */
    private static final long serialVersionUID = -3806533628945848389L;

    final private GameAccount account;
    final private ActionLatch actionLatch;
    final private GameOutcome outcome;
    final private int version;

    @JsonCreator
    public StubGameState(@JsonProperty("account") GameAccount account,
            @JsonProperty("actionLatch") ActionLatch actionLatch,
            @JsonProperty("outcome") GameOutcome outcome,
            @JsonProperty("version") int version) {
        this.account = account;
        this.actionLatch = actionLatch;
        this.outcome = outcome;
        this.version = version;
    }

    @Override
    public <State extends GameState> GameServerEvent<State> process(ClientEvent move) {
        return null;
    }

    @Override
    public GameAccount getAccount() {
        return account;
    }

    @Override
    @JsonIgnore
    public GamePlayerIterator getPlayerIterator() {
        return new SequentialPlayerIterator(ImmutableList.<Long> of(1L, 2L));
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
        result = prime * result + ((outcome == null) ? 0 : outcome.hashCode());
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
        StubGameState other = (StubGameState) obj;
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
        if (outcome == null) {
            if (other.outcome != null)
                return false;
        } else if (!outcome.equals(other.outcome))
            return false;
        if (version != other.version)
            return false;
        return true;
    }

}
