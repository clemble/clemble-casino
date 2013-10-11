package com.clemble.casino.game.event.server;

import com.clemble.casino.event.ClientEvent;
import com.clemble.casino.game.GameSession;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("playerMoved")
public class PlayerMovedEvent<State extends GameState> extends GameServerEvent<State> {

    /**
     * Generated 07/05/2013
     */
    private static final long serialVersionUID = -3272848407005579296L;

    final private ClientEvent madeMove;

    public PlayerMovedEvent(GameSession<State> session, ClientEvent madeMove) {
        super(session);
        this.madeMove = madeMove;
    }

    @JsonCreator
    public PlayerMovedEvent(@JsonProperty("session") GameSessionKey sessionKey,
            @JsonProperty("state") State state,
            @JsonProperty("madeMove") ClientEvent madeMove) {
        super(sessionKey, state);
        this.madeMove = madeMove;
    }

    public ClientEvent getMadeMove() {
        return madeMove;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((madeMove == null) ? 0 : madeMove.hashCode());
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
        PlayerMovedEvent<?> other = (PlayerMovedEvent<?>) obj;
        if (madeMove == null) {
            if (other.madeMove != null)
                return false;
        } else if (!madeMove.equals(other.madeMove))
            return false;
        return true;
    }

}
