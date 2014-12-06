package com.clemble.casino.server.event.social;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by mavarazy on 12/6/14.
 */
public class SystemSocialNotificationRequestEvent implements SystemSocialEvent {

    final public static String CHANNEL = "sys:social:notification";

    final private String player;
    final private String providerId;
    final private String notification;

    @JsonCreator
    public SystemSocialNotificationRequestEvent(
        @JsonProperty("player") String player,
        @JsonProperty("providerId") String providerId,
        @JsonProperty("notification") String notification) {
        this.player = player;
        this.providerId = providerId;
        this.notification = notification;
    }

    public String getProviderId() {
        return providerId;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    public String getNotification() {
        return notification;
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SystemSocialNotificationRequestEvent that = (SystemSocialNotificationRequestEvent) o;

        if (!notification.equals(that.notification)) return false;
        if (!player.equals(that.player)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = player.hashCode();
        result = 31 * result + notification.hashCode();
        return result;
    }
}
