package com.clemble.casino.server.event.player;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by mavarazy on 8/16/14.
 */
public class SystemPlayerImageChanged implements SystemPlayerEvent {

    final public static String CHANNEL = "player:image:update";

    final private String player;
    final private String redirect;

    @JsonCreator
    public SystemPlayerImageChanged(@JsonProperty("player") String player, @JsonProperty("redirect") String redirect) {
        this.player = player;
        this.redirect = redirect;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    public String getRedirect() {
        return redirect;
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

        SystemPlayerImageChanged that = (SystemPlayerImageChanged) o;

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
