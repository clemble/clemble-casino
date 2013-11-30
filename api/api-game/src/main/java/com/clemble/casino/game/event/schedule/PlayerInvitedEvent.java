package com.clemble.casino.game.event.schedule;

import com.clemble.casino.event.ConstructionEvent;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.construct.PlayerGameConstructionRequest;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("invited")
public class PlayerInvitedEvent implements ConstructionEvent {

    /**
     * Generated 02/06/2013
     */
    private static final long serialVersionUID = 1753173974867187325L;

    final private GameSessionKey session;

    final private PlayerGameConstructionRequest gameRequest;

    @JsonCreator
    public PlayerInvitedEvent(@JsonProperty("session") GameSessionKey session, @JsonProperty("gameRequest") PlayerGameConstructionRequest request) {
        this.session = session;
        this.gameRequest = request;
    }

    @Override
    public GameSessionKey getSession() {
        return session;
    }

    public PlayerGameConstructionRequest getGameRequest() {
        return gameRequest;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((gameRequest == null) ? 0 : gameRequest.hashCode());
        result = prime * result + (int) (session.hashCode());
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
        PlayerInvitedEvent other = (PlayerInvitedEvent) obj;
        if (gameRequest == null) {
            if (other.gameRequest != null)
                return false;
        } else if (!gameRequest.equals(other.gameRequest))
            return false;
        return session.equals(other.session);
    }
    
    @Override
    public String toString() {
        return "invited:" + session;
    }

}
