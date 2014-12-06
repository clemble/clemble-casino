package com.clemble.casino.server.event.email;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by mavarazy on 12/6/14.
 */
public class SystemEmailAddedEvent implements SystemEmailEvent {

    final public static String CHANNEL = "sys:email:added";

    final private String player;
    final private String email;
    final private boolean verified;

    @JsonCreator
    public SystemEmailAddedEvent(
        @JsonProperty(PLAYER) String player,
        @JsonProperty("email") String email,
        @JsonProperty("verified") boolean verified) {
        this.email = email;
        this.player = player;
        this.verified = verified;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    public String getEmail() {
        return email;
    }

    public boolean getVerified() {
        return verified;
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SystemEmailAddedEvent that = (SystemEmailAddedEvent) o;

        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (player != null ? !player.equals(that.player) : that.player != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = player != null ? player.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }

}
