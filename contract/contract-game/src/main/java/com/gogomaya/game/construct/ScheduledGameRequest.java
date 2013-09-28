package com.gogomaya.game.construct;

import java.util.Collection;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gogomaya.game.specification.GameSpecification;
import com.gogomaya.player.PlayerAware;

@JsonTypeName("scheduled")
public class ScheduledGameRequest extends GameRequest {

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
        super(player, specification, participants);
        this.declineBehavior = declineBehavior != null ? declineBehavior : GameDeclineBehavior.invalidate;
        this.startTime = startTime;
    }

    public GameDeclineBehavior getDeclineBehavior() {
        return declineBehavior;
    }

    public Date getStartTime() {
        return startTime;
    }

}
