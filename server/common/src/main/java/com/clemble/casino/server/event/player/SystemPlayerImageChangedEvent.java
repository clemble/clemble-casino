package com.clemble.casino.server.event.player;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by mavarazy on 8/16/14.
 */
public class SystemPlayerImageChangedEvent implements SystemPlayerEvent {

    final public static String CHANNEL = "sys:player:image:changed";

    final private String player;
    final private String redirect;
    final private String smallImage;

    @JsonCreator
    public SystemPlayerImageChangedEvent(
        @JsonProperty(PLAYER) String player,
        @JsonProperty("redirect") String redirect,
        @JsonProperty("smallImage") String smallImage) {
        this.player = player;
        this.redirect = redirect;
        this.smallImage = smallImage;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    public String getRedirect() {
        return redirect;
    }

    public String getSmallImage() {
        return smallImage;
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Override
    public String toString() {
        return "sys:" + player + ":" + CHANNEL + ":" + redirect;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SystemPlayerImageChangedEvent that = (SystemPlayerImageChangedEvent) o;

        if (!player.equals(that.player)) return false;
        if (!redirect.equals(that.redirect)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = player.hashCode();
        result = 31 * result + redirect.hashCode();
        return result;
    }
}
