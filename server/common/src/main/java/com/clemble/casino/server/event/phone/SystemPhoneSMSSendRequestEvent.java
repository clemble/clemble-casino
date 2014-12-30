package com.clemble.casino.server.event.phone;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * Created by mavarazy on 12/8/14.
 */
public class SystemPhoneSMSSendRequestEvent implements SystemPhoneEvent {

    final public static String CHANNEL = "sys:phone:sms:send";

    final private String player;
    final private String template;
    final private Map<String, String> params;

    @JsonCreator
    public SystemPhoneSMSSendRequestEvent(
        @JsonProperty(PLAYER) String player,
        @JsonProperty("template") String template,
        @JsonProperty("params") Map<String, String> params) {
        this.player = player;
        this.template = template;
        this.params = params;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    public String getTemplate() {
        return template;
    }

    public Map<String, String> getParams() {
        return params;
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
        if (!template.equals(that.template)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = player.hashCode();
        result = 31 * result + template.hashCode();
        return result;
    }

}
