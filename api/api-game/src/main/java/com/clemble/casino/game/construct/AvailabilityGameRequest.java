package com.clemble.casino.game.construct;

import java.util.Collection;

import com.clemble.casino.game.specification.GameSpecification;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.utils.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("availability")
public class AvailabilityGameRequest extends PlayerGameConstructionRequest implements GameOpponentsAware {

    /**
     * Generated 12/06/13
     */
    private static final long serialVersionUID = -3051736949418145655L;

    final private GameDeclineBehavior declineBehavior;
    final private Collection<String> participants;

    public AvailabilityGameRequest(String player, GameSpecification specification, Collection<String> participants) {
        this(player, specification, participants, GameDeclineBehavior.invalidate);
    }

    @JsonCreator
    public AvailabilityGameRequest(@JsonProperty(PlayerAware.JSON_ID) String player,
            @JsonProperty("specification") GameSpecification specification,
            @JsonProperty("participants") Collection<String> participants,
            @JsonProperty("declineBehavior") GameDeclineBehavior declineBehavior) {
        super(player, specification);
        this.declineBehavior = declineBehavior != null ? declineBehavior : GameDeclineBehavior.invalidate;
        this.participants = CollectionUtils.immutableList(participants);
    }

    public GameDeclineBehavior getDeclineBehavior() {
        return declineBehavior;
    }

    @Override
    public Collection<String> getParticipants() {
        return participants;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((declineBehavior == null) ? 0 : declineBehavior.hashCode());
        result = prime * result + ((participants == null) ? 0 : participants.hashCode());
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
        AvailabilityGameRequest other = (AvailabilityGameRequest) obj;
        if (declineBehavior != other.declineBehavior)
            return false;
        if (participants == null) {
            if (other.participants != null)
                return false;
        } else if (!participants.equals(other.participants))
            return false;
        return true;
    }


}
