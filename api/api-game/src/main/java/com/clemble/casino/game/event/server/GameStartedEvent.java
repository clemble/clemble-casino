package com.clemble.casino.game.event.server;

import com.clemble.casino.game.GameSession;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.GameSessionAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("started")
public class GameStartedEvent<State extends GameState> extends GameServerEvent<State> implements GameSessionAware {

    /**
     * Generated
     */
    private static final long serialVersionUID = -4474960027054354888L;

    public GameStartedEvent(GameSession<State> session) {
        super(session);
    }

    @JsonCreator
    public GameStartedEvent(@JsonProperty("session") GameSessionKey session, @JsonProperty("state") State state) {
        super(session, state);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (super.hashCode());
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
        GameStartedEvent<?> other = (GameStartedEvent<?>) obj;
        if (!super.equals(other))
            return false;
        return true;
    }

    @Override
    public String toString(){
        return "started:" + getSession().getGame() + ":" + getSession().getSession();
    }

}
