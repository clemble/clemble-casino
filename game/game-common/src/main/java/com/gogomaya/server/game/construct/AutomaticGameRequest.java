package com.gogomaya.server.game.construct;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gogomaya.server.game.specification.GameSpecification;

@JsonTypeName("automatic")
public class AutomaticGameRequest extends GameRequest {

    /**
     * Generated 25/06/13
     */
    private static final long serialVersionUID = -529992778342722143L;

    public AutomaticGameRequest(long playerId, GameSpecification specification) {
        super(playerId, specification);
    }

    @JsonCreator
    public AutomaticGameRequest(@JsonProperty("playerId") long playerId, @JsonProperty("specification") GameSpecification specification,
            @JsonProperty(value = "pariticipants", required = false) Collection<Long> participants) {
        super(playerId, specification);
    }

}
