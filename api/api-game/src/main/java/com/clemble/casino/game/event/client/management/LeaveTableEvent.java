package com.clemble.casino.game.event.client.management;

import com.clemble.casino.game.event.client.GameManagementEvent;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("leave")
public class LeaveTableEvent extends GameManagementEvent {

    /**
     * Generated 29/11/13
     */
    private static final long serialVersionUID = 1579387794403366281L;

    @JsonCreator
    public LeaveTableEvent(@JsonProperty("player") String player) {
        super(player);
    }

}
