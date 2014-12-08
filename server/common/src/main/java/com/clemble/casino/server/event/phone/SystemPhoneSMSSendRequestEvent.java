package com.clemble.casino.server.event.phone;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by mavarazy on 12/8/14.
 */
public class SystemPhoneSMSSendRequestEvent implements SystemPhoneEvent {

    final public static String CHANNEL = "sys:phone:sms:send";

    final private String player;
    final private String text;

    @JsonCreator
    public SystemPhoneSMSSendRequestEvent(
        @JsonProperty(PLAYER) String player,
        @JsonProperty("text") String text) {
        this.player = player;
        this.text = text;
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

        SystemPhoneSMSSendRequestEvent that = (SystemPhoneSMSSendRequestEvent) o;

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
