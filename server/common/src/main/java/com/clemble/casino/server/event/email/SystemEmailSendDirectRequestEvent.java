package com.clemble.casino.server.event.email;

import com.clemble.casino.server.event.TemplateAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.Map;

/**
 * Created by mavarazy on 2/2/15.
 */
public class SystemEmailSendDirectRequestEvent implements SystemEmailEvent, TemplateAware {

    final public static String CHANNEL = "sys:email:send:direct";

    final private String player;
    final private String email;
    final private String template;
    final private Map<String, String> params;

    @JsonCreator
    public SystemEmailSendDirectRequestEvent(
        @JsonProperty(PLAYER) String player,
        @JsonProperty("email") String email,
        @JsonProperty("template") String template,
        @JsonProperty("params") Map<String, String> params) {
        this.player = player;
        this.email = email;
        this.template = template;
        this.params = params;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getTemplate() {
        return template;
    }

    public Map<String, String> getParams() {
        return params;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SystemEmailSendDirectRequestEvent)) return false;

        SystemEmailSendDirectRequestEvent that = (SystemEmailSendDirectRequestEvent) o;

        if (!email.equals(that.email)) return false;
        if (!params.equals(that.params)) return false;
        if (!player.equals(that.player)) return false;
        if (!template.equals(that.template)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = player.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + template.hashCode();
        result = 31 * result + params.hashCode();
        return result;
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
