package com.gogomaya.server.game.event.client.surrender;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("giveUp")
public class GiveUpEvent extends SurrenderEvent {

    /**
     * Generated 29/05/13
     */
    private static final long serialVersionUID = 4501169964446540650L;

    @JsonCreator
    public GiveUpEvent(@JsonProperty("player") long playerId) {
        super(playerId);
    }

}
