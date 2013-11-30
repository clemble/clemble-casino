package com.clemble.casino.game.construct;

import java.util.Collection;
import java.util.Date;

import com.clemble.casino.game.specification.GameSpecification;
import com.clemble.casino.player.PlayerAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("scheduled")
public class ScheduledGameRequest extends PlayerGameConstructionRequest {

    /**
     * Generated 10/06/13
     */
    private static final long serialVersionUID = -5026198091605671710L;

    final private GameDeclineBehavior declineBehavior;

    final private Date startTime;

    @JsonCreator
    public ScheduledGameRequest(@JsonProperty(PlayerAware.JSON_ID) String player,
            @JsonProperty("specification") GameSpecification specification,
            @JsonProperty("participants") Collection<String> participants,
            @JsonProperty("declineBehavior") GameDeclineBehavior declineBehavior,
            @JsonProperty("startTime") Date startTime) {
        super(player, specification);
        this.declineBehavior = declineBehavior != null ? declineBehavior : GameDeclineBehavior.invalidate;
        this.startTime = startTime;
    }

    public GameDeclineBehavior getDeclineBehavior() {
        return declineBehavior;
    }

    public Date getStartTime() {
        return startTime;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((declineBehavior == null) ? 0 : declineBehavior.hashCode());
        result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        ScheduledGameRequest other = (ScheduledGameRequest) obj;
        if (declineBehavior != other.declineBehavior)
            return false;
        if (startTime == null) {
            if (other.startTime != null)
                return false;
        } else if (!startTime.equals(other.startTime))
            return false;
        return true;
    }

}
