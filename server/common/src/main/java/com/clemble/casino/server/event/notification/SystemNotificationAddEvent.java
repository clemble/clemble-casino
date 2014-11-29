package com.clemble.casino.server.event.notification;

import com.clemble.casino.lifecycle.configuration.rule.privacy.PrivacyRule;
import com.clemble.casino.lifecycle.configuration.rule.privacy.PrivacyRuleAware;
import com.clemble.casino.notification.PlayerNotification;
import com.clemble.casino.notification.PlayerNotificationAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by mavarazy on 11/29/14.
 */
public class SystemNotificationAddEvent implements SystemNotificationEvent, PlayerNotificationAware, PrivacyRuleAware {

    final public static String CHANNEL = "sys:notification:add";

    final private PrivacyRule privacyRule;
    final private PlayerNotification notification;

    @JsonCreator
    public SystemNotificationAddEvent(
        @JsonProperty("privacyRule") PrivacyRule privacyRule,
        @JsonProperty("notification") PlayerNotification notification) {
        this.notification = notification;
        this.privacyRule = privacyRule;
    }

    @Override
    public PlayerNotification getNotification() {
        return notification;
    }

    @Override
    public PrivacyRule getPrivacyRule() {
        return privacyRule;
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
        if (privacyRule != that.privacyRule) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = privacyRule.hashCode();
        result = 31 * result + notification.hashCode();
        return result;
    }
}
