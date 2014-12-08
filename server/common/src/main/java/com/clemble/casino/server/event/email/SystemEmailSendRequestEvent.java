package com.clemble.casino.server.event.email;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by mavarazy on 12/8/14.
 */
public class SystemEmailSendRequestEvent implements SystemEmailEvent{

    final public static String CHANNEL = "sys:event:send";

    final private String player;
    final private String text;

    @JsonCreator
    public SystemEmailSendRequestEvent(@JsonProperty(PLAYER) String player, @JsonProperty("text") String text) {
        this.text = text;
        this.player = player;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    public String getText() {
        return text;
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SystemEmailSendRequestEvent that = (SystemEmailSendRequestEvent) o;

        if (!player.equals(that.player)) return false;
        if (!text.equals(that.text)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = player.hashCode();
        result = 31 * result + text.hashCode();
        return result;
    }

}
