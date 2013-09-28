package com.gogomaya.game.event.client.surrender;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gogomaya.player.PlayerAware;

@JsonTypeName("totalTimeBreached")
public class TotalTimeoutSurrenderEvent extends SurrenderEvent {

    /**
     * Generated 10/06/13
     */
    private static final long serialVersionUID = 6999945454488627240L;

    @JsonCreator
    public TotalTimeoutSurrenderEvent(@JsonProperty(PlayerAware.JSON_ID) String playerId) {
        super(playerId);
    }

}
