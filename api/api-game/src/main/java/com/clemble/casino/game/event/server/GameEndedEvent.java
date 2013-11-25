package com.clemble.casino.game.event.server;

import com.clemble.casino.game.GameSession;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.outcome.GameOutcome;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("ended")
public class GameEndedEvent<State extends GameState> extends GameServerEvent<State> {

    /**
     * Generated 07/05/13
     */
    private static final long serialVersionUID = 820200145932972096L;

    final private GameOutcome outcome;

    public GameEndedEvent(GameSession<State> session, GameOutcome outcome) {
        super(session);
        this.outcome = outcome;
    }

    @JsonCreator
    public GameEndedEvent(@JsonProperty("session") GameSessionKey sessionKey, @JsonProperty("state") State state, @JsonProperty("outcome") GameOutcome outcome) {
        super(sessionKey, state);
        this.outcome = outcome;
    }
    
    public GameOutcome getOutcome() {
        return outcome;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((outcome == null) ? 0 : outcome.hashCode());
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
        GameEndedEvent<?> other = (GameEndedEvent<?>) obj;
        if (outcome == null) {
            if (other.outcome != null)
                return false;
        } else if (!outcome.equals(other.outcome))
            return false;
        return true;
    }

    @Override
    public String toString(){
        return  "ended:" + getSession();
    }
}
