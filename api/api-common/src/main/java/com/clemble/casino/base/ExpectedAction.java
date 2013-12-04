package com.clemble.casino.base;

import com.clemble.casino.event.ClientEvent;
import com.clemble.casino.player.PlayerAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("expected")
public class ExpectedAction implements ClientEvent {

    /**
     * Generated 02/07/13
     */
    private static final long serialVersionUID = 6497446081286294728L;

    final private String player;
    final private String action;

    @JsonCreator
    public ExpectedAction(@JsonProperty(PlayerAware.JSON_ID) String player, @JsonProperty("action") String action) {
        this.player = player;
        this.action = action;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    public String getAction() {
        return action;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((action == null) ? 0 : action.hashCode());
        result = prime * result + ((player == null) ? 0 : player.hashCode());
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
        return player.equals(other.player);
    }

}
