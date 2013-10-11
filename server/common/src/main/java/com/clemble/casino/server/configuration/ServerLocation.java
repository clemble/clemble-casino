package com.clemble.casino.server.configuration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ServerLocation {

    final private String location;

    @JsonCreator
    public ServerLocation(@JsonProperty("location") String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

}
