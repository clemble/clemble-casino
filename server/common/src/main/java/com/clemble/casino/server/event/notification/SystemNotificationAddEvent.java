package com.clemble.casino.server.event.notification;

import com.clemble.casino.notification.PlayerNotification;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by mavarazy on 11/29/14.
 */
public class SystemNotificationAddEvent implements SystemNotificationEvent {

    final public static String CHANNEL = "sys:notification:add";

    final private PlayerNotification notification;

    @JsonCreator
    public SystemNotificationAddEvent(
        @JsonProperty("notification") PlayerNotification notification) {
        this.notification = notification;
    }

    @Override
    public PlayerNotification getNotification() {
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

        SystemNotificationAddEvent that = (SystemNotificationAddEvent) o;

        if (!notification.equals(that.notification)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return notification.hashCode();
    }

}
