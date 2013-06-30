package com.gogomaya.server.game.construct;

import java.util.Collection;
import java.util.Collections;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("automatic")
public class AutomaticGameRequest extends GameRequest {

    /**
     * Generated 25/06/13
     */
    private static final long serialVersionUID = -529992778342722143L;

    public Collection<Long> getParticipants() {
        return Collections.singleton(getPlayerId());
    }

}
