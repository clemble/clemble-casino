package com.gogomaya.game.construct;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gogomaya.game.specification.GameSpecification;

@JsonTypeName("availability")
public class AvailabilityGameRequest extends GameRequest {

    /**
     * Generated 12/06/13
     */
    private static final long serialVersionUID = -3051736949418145655L;

    final private GameDeclineBehavior declineBehavior;

    public AvailabilityGameRequest(long playerId, GameSpecification specification, Collection<Long> participants) {
        this(playerId, specification, participants, GameDeclineBehavior.invalidate);
    }

    @JsonCreator
    public AvailabilityGameRequest(@JsonProperty("playerId") long playerId, @JsonProperty("specification") GameSpecification specification,
            @JsonProperty("participants") Collection<Long> participants, @JsonProperty("declineBehavior") GameDeclineBehavior declineBehavior) {
        super(playerId, specification, participants);
        this.declineBehavior = declineBehavior != null ? declineBehavior : GameDeclineBehavior.invalidate;
    }

    public GameDeclineBehavior getDeclineBehavior() {
        return declineBehavior;
    }

}
