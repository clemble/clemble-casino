package com.clemble.casino.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("guest")
public class GuestPlayerProfile extends PlayerProfile {

    /**
     * Generated 04/11/13
     */
    private static final long serialVersionUID = -5880057697031644712L;

    @Override
    @JsonIgnore
    public int getVersion() {
        return 0;
    }

}
