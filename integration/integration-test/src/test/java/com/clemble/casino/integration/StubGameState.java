package com.clemble.casino.integration;

import org.apache.commons.lang3.RandomStringUtils;

import com.clemble.casino.base.ActionLatch;
import com.clemble.casino.event.ClientEvent;
import com.clemble.casino.game.GameSession;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.account.GameAccount;
import com.clemble.casino.game.event.server.GameServerEvent;
import com.clemble.casino.game.iterator.GamePlayerIterator;
import com.clemble.casino.game.iterator.SequentialPlayerIterator;
import com.clemble.casino.game.outcome.GameOutcome;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
    public <State extends GameState> GameServerEvent<State> process(GameSession<State> session, ClientEvent move) {
        return null;
    }

    @Override
    public GameAccount getAccount() {
        return account;
    }

    @Override
    @JsonIgnore
    public GamePlayerIterator getPlayerIterator() {
        return new SequentialPlayerIterator(ImmutableList.<String> of(RandomStringUtils.random(5), RandomStringUtils.random(5)));
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
