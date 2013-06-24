package com.gogomaya.server.game.event.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("moveTimeBreached")
public class MoveTimeoutSurrenderEvent extends SurrenderEvent {

    /**
     * Generated 10/06/13
     */
    private static final long serialVersionUID = -3052155086475447441L;

    @JsonCreator
    public MoveTimeoutSurrenderEvent(@JsonProperty("playerId") long playerId) {
        super(playerId);
    }

}
