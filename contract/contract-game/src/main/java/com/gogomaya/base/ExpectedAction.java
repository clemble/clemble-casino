package com.gogomaya.base;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gogomaya.event.ClientEvent;

@JsonTypeName("expected")
public class ExpectedAction implements ClientEvent {

    /**
     * Generated 02/07/13
     */
    private static final long serialVersionUID = 6497446081286294728L;

    final private long playerId;
    final private String action;

    @JsonCreator
    public ExpectedAction(@JsonProperty("playerId") long playerId, @JsonProperty("action") String action) {
        this.playerId = playerId;
        this.action = action;
    }

    @Override
    public long getPlayerId() {
        return playerId;
    }

    public String getAction() {
        return action;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((action == null) ? 0 : action.hashCode());
        result = prime * result + (int) (playerId ^ (playerId >>> 32));
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
        ExpectedAction other = (ExpectedAction) obj;
        if (action == null) {
            if (other.action != null)
                return false;
        } else if (!action.equals(other.action))
            return false;
        if (playerId != other.playerId)
            return false;
        return true;
    }

}
