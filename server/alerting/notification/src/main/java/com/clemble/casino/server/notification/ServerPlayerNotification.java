package com.clemble.casino.server.notification;

import com.clemble.casino.notification.PlayerNotification;
import com.clemble.casino.player.PlayerAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * Created by mavarazy on 11/29/14.
 */
public class ServerPlayerNotification implements PlayerAware {

    @Id
    final private String key;
    final private String player;
    final private PlayerNotification notification;
    final private Date created;

    @JsonCreator
    public ServerPlayerNotification(
        @JsonProperty("key") String key,
        @JsonProperty(PLAYER) String player,
        @JsonProperty("notification") PlayerNotification notification,
        @JsonProperty("created") Date created) {
        this.key = key;
        this.player = player;
        this.notification = notification;
        this.created = created;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    public PlayerNotification getNotification() {
        return notification;
    }

    public Date getCreated() {
        return created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServerPlayerNotification that = (ServerPlayerNotification) o;

        if (!created.equals(that.created)) return false;
        if (!notification.equals(that.notification)) return false;
        if (!player.equals(that.player)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = player.hashCode();
        result = 31 * result + notification.hashCode();
        result = 31 * result + created.hashCode();
        return result;
    }

}
