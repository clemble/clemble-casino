package com.clemble.casino.game.event.client.surrender;

import com.clemble.casino.player.PlayerAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("moveTimeout")
public class MoveTimeoutSurrenderEvent extends SurrenderEvent {

    /**
     * Generated 10/06/13
     */
    private static final long serialVersionUID = -3052155086475447441L;

    @JsonCreator
    public MoveTimeoutSurrenderEvent(@JsonProperty(PlayerAware.JSON_ID) String player) {
        super(player);
    }

}
