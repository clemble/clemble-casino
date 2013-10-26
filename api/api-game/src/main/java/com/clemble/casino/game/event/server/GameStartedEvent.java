package com.clemble.casino.game.event.server;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import com.clemble.casino.game.GameSession;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.ServerResourse;
import com.clemble.casino.game.SessionAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("started")
public class GameStartedEvent<State extends GameState> extends GameServerEvent<State> implements SessionAware {

    /**
     * Generated
     */
    private static final long serialVersionUID = -4474960027054354888L;

    final private ServerResourse resource;

    public GameStartedEvent(GameSession<State> session, ServerResourse resourse) {
        super(session);
        this.resource = checkNotNull(resourse);
    }

    @JsonCreator
    public GameStartedEvent(@JsonProperty("session") GameSessionKey session, @JsonProperty("state") State state, @JsonProperty("resource") ServerResourse resource) {
        super(session, state);
        this.resource = resource;
    }

    public ServerResourse getResource() {
        return resource;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (super.hashCode());
        result = prime * result + ((resource == null) ? 0 : resource.hashCode());
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
        if (!super.equals(obj))
            return false;
        if (resource == null) {
            if (other.resource != null)
                return false;
        } else if (!resource.equals(other.resource))
            return false;
        return true;
    }

}
